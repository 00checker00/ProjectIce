package de.project.ice.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ScreenViewport
import de.project.ice.IceGame
import de.project.ice.Storage
import de.project.ice.config.Config
import de.project.ice.dialog.Node
import de.project.ice.ecs.systems.SoundSystem
import de.project.ice.utils.DelegatingBlockingInputProcessor
import java.util.*

class DialogScreen(game: IceGame, dialog: Node) : BaseScreenAdapter(game) {
    private val stage = Stage()
    private val skin = Skin(Gdx.files.internal("ui/skin.json"))
    private val root = Table()
    private val buttons = HashMap<String, TextButton>()
    private var choiceTable: Table? = null
    public override val inputProcessor: InputProcessor = DialogScreenInputProcessor(stage)

    init {
        if (Config.RENDER_DEBUG) {
            stage.setDebugAll(true)
        }

        stage.viewport = ScreenViewport()

        stage.addActor(root)

        root.setFillParent(true)

        showNode(dialog)
    }

    private fun showNode(node: Node?) {
        if (node == null) {
            // Reached end of dialog
            game.removeScreen(this)
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
        clear()
        root.row().expand()

        choiceTable = Table(skin)
        choiceTable!!.defaults().space(20f)
        choiceTable!!.defaults().align(Align.left)

        if (node.choices.size > 0) {
            var pad = 50f

            for (pair in node.choices) {
                val choice = pair.first
                choiceTable!!.row()
                val text: String
                try {
                    text = game.strings.get(choice.text)
                } catch (ignore: MissingResourceException) {
                    // Missing a translation line => fallback to text id
                    text = choice.text
                    Gdx.app.log(javaClass.simpleName, "Missing translation for: " + choice.text)
                }
                val btn = TextButton(text, skin)
                val next = choice.next
                btn.addListener(object : InputListener() {
                    override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                        showNode(next)
                        game.engine.soundSystem.playSound(choice.text, SoundSystem.Type.Voice)
                        return true
                    }
                })
                pad /= 2f
                choiceTable!!.add(btn).padLeft(pad)
            }
        }

        root.add<Table>(choiceTable).top().left()

        root.row().expandX()

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

            game.engine.soundSystem.playSound(node.text, SoundSystem.Type.Voice)
            val textLabel = Label(text, skin)
            textLabel.setAlignment(Align.center)
            val scrollPane = ScrollPane(textLabel, skin)
            root.add(scrollPane).height(50f).fill()
        }

    }

    private fun clear() {
        root.clear()
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
}
