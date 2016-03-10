package de.project.ice.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.viewport.ScreenViewport
import de.project.ice.IceGame
import de.project.ice.config.Config
import de.project.ice.utils.DelegatingBlockingInputProcessor
import de.project.ice.utils.FreetypeSkin
import java.util.*

open class MainMenuScreen(game: IceGame) : BaseScreenAdapter(game) {
    private val stage = Stage()
    private val skin = FreetypeSkin(Gdx.files.internal("ui/skin.json"))
    private val root = Table()
    private val menuLayout = VerticalGroup()
    private val buttons = HashMap<String, TextButton>()
    override val inputProcessor: InputProcessor = DelegatingBlockingInputProcessor(stage)

    init {

        if (Config.RENDER_DEBUG) {
            stage.setDebugAll(true)
        }
        stage.viewport = ScreenViewport()

        stage.addActor(root)

        root.setFillParent(true)
        root.background = skin.getTiledDrawable("menu_bg")

        val logo = Image(skin, "menu_logo")
        root.add(logo)

        root.row()

        menuLayout.fill()
        menuLayout.space(5f)
        root.add(menuLayout)

        createMenuButton(BUTTON_NEW_GAME_ID, "New Game", object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                this@MainMenuScreen.game.startNewGame()
                this@MainMenuScreen.game.removeScreen(this@MainMenuScreen)
                return true
            }
        })
        createMenuButton(BUTTON_SAVE_LOAD_ID, "Save/Load", object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                println("Save/Load")
                return true
            }
        })
        createMenuButton(BUTTON_SETTINGS_ID, "Settings", object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                println("Settings")
                return true
            }
        })
        createMenuButton(BUTTON_EXIT_ID, "Exit", object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                this@MainMenuScreen.game.exit()
                return true
            }
        })
    }

    override val priority: Int
        get() = 100

    protected fun insertMenuButton(id: String, text: String, listener: InputListener?, idAfter: String) {
        val button = TextButton(text, skin)
        if (listener != null) {
            button.addListener(listener)
        }
        menuLayout.addActorAfter(buttons[idAfter], button)
        buttons.put(id, button)
    }

    protected fun createMenuButton(id: String, text: String, listener: InputListener?) {
        val button = TextButton(text, skin)
        if (listener != null) {
            button.addListener(listener)
        }
        menuLayout.addActor(button)
        buttons.put(id, button)
    }

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

    companion object {
        val BUTTON_NEW_GAME_ID = "BTN_NEW_GAME"
        val BUTTON_SAVE_LOAD_ID = "BTN_SAVE_LOAD"
        val BUTTON_EXIT_ID = "BTN_EXIT"
        val BUTTON_SETTINGS_ID = "BTN_SETTINGS"
    }
}
