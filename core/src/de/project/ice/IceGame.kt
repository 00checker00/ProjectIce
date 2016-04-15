package de.project.ice

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.I18NBundle
import de.project.ice.dialog.Dialog
import de.project.ice.dialog.Node
import de.project.ice.ecs.IceEngine
import de.project.ice.inventory.Inventory
import de.project.ice.screens.*
import de.project.ice.utils.Assets
import de.project.ice.utils.SceneLoader

import java.io.IOException
import java.util.*

open class IceGame : ApplicationAdapter() {
    private val screens = Array<BaseScreen>()
    private val screensToAdd = Array<BaseScreen>()
    private val screensToRemove = Array<Pair<BaseScreen, Boolean>>()
    protected val gameScreen: GameScreen by lazy { GameScreen(this, engine) }
    protected val cursorScreen: CursorScreen by lazy { CursorScreen(this) }
    protected val inputProcessor = InputMultiplexer()
    val engine: IceEngine by lazy { IceEngine(this) }
    val inventory = Inventory(this)
    val strings: I18NBundle by lazy { I18NBundle.createBundle(Gdx.files.internal("strings/ProjectIce")) }
    var isGamePaused = false
        private set
    var BlockInteraction = false
        set(value) {
            field = value
            engine.controlSystem.setProcessing(!value)
        }
    var BlockSaving = false;

    override fun create() {
        I18NBundle.setSimpleFormatter(true)

        addScreen(gameScreen)
        addScreen(cursorScreen)

        init()

    }

    protected open fun init() {
        addScreen(MainMenuScreen(this))
        Gdx.input.inputProcessor = inputProcessor
    }

    fun pauseGame() {
        gameScreen.pauseGame()
        isGamePaused = true
    }

    fun resumeGame() {
        gameScreen.resumeGame()
        isGamePaused = false
    }

    fun showDialog(node: Node, callback: ()->Unit = {}) {
        addScreen(DialogScreen(this, node).apply {
            this.callback = callback
        })
    }

    fun showDialog(dialog: String, callback: ()->Unit = {}) {
        showDialog(Dialog.load(Gdx.files.internal("dialog/$dialog.dlz")), callback)
    }


    fun showMessages(vararg messages: String) {
        addScreen(MessageScreen(this, *messages.map {
            try { strings.get(it) } catch (ex: MissingResourceException) { "$$$it$$" } }.toTypedArray()))
    }

    override fun dispose() {
        for (screen in screens) {
            screen.hide()
            screen.dispose()
        }
        screens.clear()
    }

    override fun pause() {
        if (screens.size > 0) {
            screens.peek().pause()
        }
    }

    override fun resume() {
        if (screens.size > 0) {
            screens.peek().resume()
        }
    }

    var primaryCursor: CursorScreen.Cursor
        get() = cursorScreen.primaryCursor
        set(primaryCursor) {
            cursorScreen.primaryCursor = primaryCursor
        }

    var secondaryCursor: CursorScreen.Cursor
        get() = cursorScreen.secondaryCursor
        set(secondaryCursor) {
            cursorScreen.secondaryCursor = secondaryCursor
        }

    var cursorText: String
        get() = cursorScreen.cursorText
        set(cursorText) {
            cursorScreen.cursorText = cursorText
        }

    override fun render() {
        Assets.update()

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
        val delta = Gdx.graphics.deltaTime
        for (screen in screens) {
            screen.update(delta)
        }
        for (screen in screens) {
            screen.render()
        }
        for (screen in screensToAdd) {
            if (screens.contains(screen, true)) {
                continue
            }

            var index = -1
            for (i in 0..screens.size - 1) {
                if (screens.get(i).priority < screen.priority) {
                    index = i
                    break
                }
            }
            if (index == -1) {
                if (screens.size > 0) {
                    screens.peek().pause()
                }
                screens.add(screen)
                screen.show()
            } else {
                screens.insert(index, screen)
            }
            screen.resize(Gdx.graphics.width, Gdx.graphics.height)
        }
        screensToAdd.clear()
        for (pair in screensToRemove) {
            if (pair === screens.peek()) {
                screens.pop()
                screens.peek().resume()
            } else {
                screens.removeValue(pair.first, true)
            }
            pair.first.hide()
            if (pair.second) {
                pair.first.dispose()
            }
        }
        screensToRemove.clear()
    }

    override fun resize(width: Int, height: Int) {
        for (screen in screens) {
            screen.resize(width, height)
        }
    }

    @JvmOverloads fun removeScreen(screen: BaseScreen, dispose: Boolean = true) {
        screensToRemove.add(Pair(screen, dispose))
    }

    fun addScreen(screen: BaseScreen) {
        screensToAdd.add(screen)
    }

    fun isScreenVisible(screen: BaseScreen): Boolean {
        return screens.contains(screen, true)
    }

    fun startNewGame() {
        engine.removeAllEntities()
        try {
            SceneLoader.loadScene(engine, Gdx.files.internal("scenes/scene2.scene"))
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: SceneLoader.LoadException) {
            e.printStackTrace()
        }

    }

    /**
     * Exit the game. Cleans up all the resources
     */
    fun exit() {
        //TODO: Actually clean up the resources
        Gdx.app.exit()
    }

    inner class InputMultiplexer : InputProcessor {
        override fun keyDown(keycode: Int): Boolean {
            for (i in screens.size - 1 downTo 0) {
                if (screens.get(i).inputProcessor.keyDown(keycode)) {
                    return true
                }
            }
            return false
        }

        override fun keyUp(keycode: Int): Boolean {
            for (i in screens.size - 1 downTo 0) {
                if (screens.get(i).inputProcessor.keyUp(keycode)) {
                    return true
                }
            }
            return false
        }

        override fun keyTyped(character: Char): Boolean {
            for (i in screens.size - 1 downTo 0) {
                if (screens.get(i).inputProcessor.keyTyped(character)) {
                    return true
                }
            }
            return false
        }

        override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
            for (i in screens.size - 1 downTo 0) {
                if (screens.get(i).inputProcessor.touchDown(screenX, screenY, pointer, button)) {
                    return true
                }
            }
            return false
        }

        override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
            for (i in screens.size - 1 downTo 0) {
                if (screens.get(i).inputProcessor.touchUp(screenX, screenY, pointer, button)) {
                    return true
                }
            }
            return false
        }

        override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
            for (i in screens.size - 1 downTo 0) {
                if (screens.get(i).inputProcessor.touchDragged(screenX, screenY, pointer)) {
                    return true
                }
            }
            return false
        }

        override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
            for (i in screens.size - 1 downTo 0) {
                if (screens.get(i).inputProcessor.mouseMoved(screenX, screenY)) {
                    return true
                }
            }
            return false
        }

        override fun scrolled(amount: Int): Boolean {
            for (i in screens.size - 1 downTo 0) {
                if (screens.get(i).inputProcessor.scrolled(amount)) {
                    return true
                }
            }
            return false
        }
    }
}
