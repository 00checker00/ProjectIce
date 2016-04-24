package de.project.ice.screens

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ScreenViewport
import de.project.ice.IceGame
import de.project.ice.Storage
import de.project.ice.config.Config
import de.project.ice.dialog.Node
import de.project.ice.ecs.Components
import de.project.ice.ecs.components.SpeakComponent
import de.project.ice.ecs.getComponents
import de.project.ice.ecs.systems.SoundSystem
import de.project.ice.utils.*
import java.util.*

class DialogScreen(game: IceGame, dialog: Node) : BaseScreenAdapter(game) {
    private val stage = Stage()
    private val skin = DefaultSkin
    private val font = skin.getFont("dialogFont")
    private val textLayout = GlyphLayout()

    private val choices = Table(skin)
    private val root = Container<Stack>()
    private val buttons = HashMap<String, TextButton>()
    override val inputProcessor: InputProcessor = DialogScreenInputProcessor(stage)
    var callback: (()->Unit)? = null
    val dialogEntities: ImmutableArray<Entity>

    private var dialogueSoundID: Long = -1L

    init {
        game.primaryCursor = CursorScreen.Cursor.None
        game.secondaryCursor = CursorScreen.Cursor.None
        game.cursorText = ""

        if (Config.RENDER_DEBUG) {
            stage.setDebugAll(true)
        }

        stage.viewport = ScreenViewport()

        val backgroundImage = skin.getRegion("ui_dialogBox")
        val background = Image(backgroundImage)

        val stack = Stack(background, choices)

        choices.apply {
            setFillParent(true)
            top()
            pad(64f, 100f, 64f, 100f)
        }

        stage.addActor(root)

        root.apply {
            actor = stack
            setFillParent(true)
            bottom()
            fillX()
            layout()
        }

        dialogEntities = game.engine.getEntitiesFor(Family.all(SpeakComponent::class.java).get())

        showNode(dialog)
    }

    private fun showNode(node: Node?) {
        if (node == null) {
            // Reached end of dialog
            game.removeScreen(this)
            callback?.invoke()
            return
        } else if ((node.type == Node.Type.Node || node.type == Node.Type.Start) && node.choices.size == 0) {
            // Empty node => Skip to next
            showNode(node.next)
            return
        } else if (node.type == Node.Type.Set) {
            // Set value then skip to next
            Storage.SAVESTATE.put(node.variable_name!!, node.variable_value!!)
            showNode(node.next)
            return
        } else if (node.type == Node.Type.Branch) {
            // Switch to correct branch
            val next: Node?
            if (node.branch == null) {
                next = null
            } else {
                next = node.branch!!.getForValue(Storage.SAVESTATE.getString(node.branch!!.variable_name))
            }
            showNode(next)
            return
        }
        dialogEntities.forEach { game.engine.removeEntity(it) }

        choices.clear()
        if (node.choices.size > 0) {
            root.isVisible = true
            for (pair in node.choices) {
                val choice = pair.first
                choices.row()
                val text: String
                try {
                    text = game.strings.get(choice.text)
                } catch (ignore: MissingResourceException) {
                    // Missing a translation line => fallback to text id
                    text = choice.text
                    Gdx.app.log(javaClass.simpleName, "Missing translation for: " + choice.text)
                }
                val btn = TextButton(text, skin, "dialogChoice")
                btn.label.setAlignment(Align.left)
                val next = choice.next
                btn.addListener(object : InputListener() {
                    override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                        showNode(next)
                        game.engine.soundSystem.playSound(choice.text, SoundSystem.Type.Voice)
                        return true
                    }
                })
                choices.add(btn).fillX().expandX().top().left()
            }
        } else {
            root.isVisible = false
        }

        (inputProcessor as DialogScreenInputProcessor).next = node.next
        inputProcessor.nextEnabled = node.choices.size == 0
        if (!node.text.isEmpty()) {
            val text: String
            try {
                text = game.strings.get(node.text)
            } catch (ignore: MissingResourceException) {
                // Missing a translation line => fallback to text id
                text = node.text
                Gdx.app.log(javaClass.simpleName, "Missing translation for: " + node.text)
            }

            game.engine.soundSystem.stopSound(dialogueSoundID)
            dialogueSoundID = game.engine.soundSystem.playSound(node.text, SoundSystem.Type.Voice)

            game.engine.addEntity {
                SpeakComponent {
                    this.text = text
                    this.targetName = node.speaker
                    this.color = node.color
                }
            }
        }

    }

    override val priority: Int
        get() = 700

    protected fun getButton(id: String): TextButton {
        return buttons[id]!!
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun update(delta: Float) {
        stage.act(delta)
    }

    override fun render() {
        stage.viewport.apply()

        val camera = game.engine.renderingSystem.activeCamera

        if (camera != null) {

            stage.batch.begin()

            for (dialogLine in dialogEntities.map { it.getComponents(Components.speak) }) {
                if (dialogLine.target == null)
                    continue

                val (targetTransform, targetHotspot) = dialogLine.target!!.getComponents(Components.transform, Components.hotspot)

                val targetX = targetTransform.pos.x
                val targetY = targetTransform.pos.y + targetHotspot.height

                val projected = camera.camera.project(Vector3(targetX, targetY, 0f))

                var y = projected.y + font.lineHeight * 1.2f
                for (line in dialogLine.text.split("\n").reversed()) {
                    font.color = dialogLine.color;
                    textLayout.setText(font, line)

                    var x = projected.x - textLayout.width/2

                    val size = camera.camera.project(Vector3(camera.camera.viewportWidth, camera.camera.viewportHeight, 0f))

                    if (x < MIN_SCREEN_DIST)
                        x = MIN_SCREEN_DIST
                    else if (x > size.x - textLayout.width - MIN_SCREEN_DIST)
                        x = size.x - textLayout.width - MIN_SCREEN_DIST


                    font.draw(stage.batch, textLayout, x , y )

                    y += textLayout.height * 1.5f
                }


            }

            stage.batch.end()
        }

        stage.draw()
    }

    override fun dispose() {
        stage.dispose()
    }

    private inner class DialogScreenInputProcessor(processor: com.badlogic.gdx.InputProcessor) : DelegatingBlockingInputProcessor(processor) {
        var next: Node? = null
        var nextEnabled = false

        override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
            if (!nextEnabled) {
                return super.touchDown(screenX, screenY, pointer, button)
            }

            showNode(next)
            return true
        }
    }

    companion object {
        val MIN_SCREEN_DIST = 30f
    }
}
