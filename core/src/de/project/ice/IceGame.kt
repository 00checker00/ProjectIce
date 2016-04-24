package de.project.ice

import com.badlogic.gdx.*
import com.badlogic.gdx.graphics.Color
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
import de.project.ice.utils.SceneWriter

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
    var blockInteraction = false
        set(value) {
            field = value
            engine.controlSystem.setProcessing(!value)
        }
    var blockSaving = false
    var currentSaveSlot: Int = 0; private set

    override fun create() {
        I18NBundle.setSimpleFormatter(true)

        addScreen(gameScreen)
        addScreen(cursorScreen)

        init()

        Storage.SAVESTATE.load(currentSaveSlot)
    }

    protected open fun init() {
        addScreen(MainMenuScreen(this))
        Gdx.input.inputProcessor = inputProcessor

        SceneLoader.loadScene(engine, Gdx.files.internal("scenes/Hauptmenue.scene"))
    }

    fun pauseGame() {
        gameScreen.pauseGame()
        isGamePaused = true
    }

    fun resumeGame() {
        gameScreen.resumeGame()
        isGamePaused = false
    }

    fun save(slot: Int = currentSaveSlot): Boolean {
        var result = false
        if (!blockSaving) {
            val xmlState = SceneWriter.serializeToString(engine, engine.sceneProperties!!)
            Storage.SAVESTATE.put("__SAVESTATE__", xmlState)
            Storage.SAVESTATE.save(slot)
            result = true
        }
        currentSaveSlot = slot
        return result
    }

    fun load(slot: Int = currentSaveSlot, blendScreen: Boolean = false): Boolean {
        var result = false
        if (!blockSaving) {
            if (slot != currentSaveSlot)
                Storage.SAVESTATE.load(slot)

            if (hasSave()) {
                val xmlState = Storage.SAVESTATE.getString("__SAVESTATE__")
                if (blendScreen) {
                    engine.blendScreen(2.0f, Color.BLACK)

                    engine.timeout(2.0f) {
                        engine.entities.forEach { engine.removeEntity(it) }
                        engine.timeout(0f) {
                            try {
                                SceneLoader.loadScene(engine, xmlState)
                            } catch (e: IOException) {
                                e.printStackTrace()
                            } catch (e: SceneLoader.LoadException) {
                                e.printStackTrace()
                            }
                        }
                    }
                } else {
                    SceneLoader.loadScene(engine, xmlState)
                }
                result = true
            }
        }
        currentSaveSlot = slot
        return result
    }

    fun hasSave(slot: Int = currentSaveSlot): Boolean {
        if (!blockSaving)
            if (slot != currentSaveSlot)
                Storage.SAVESTATE.load(slot)

        return Storage.SAVESTATE.hasKey("__SAVESTATE__")
    }

    fun showDialog(node: Node, callback: ()->Unit = {}): DialogHandle {
        return DialogHandle(addScreen(DialogScreen(this, node).apply {
            this.callback = callback
        }), this)
    }

    fun showDialog(dialog: String, callback: ()->Unit = {}): DialogHandle {
        return showDialog(Dialog.load(Gdx.files.internal("dialog/$dialog.dlz")), callback)
    }


    fun showToastMessages(vararg messages: String) {
        val messageStrings = messages.map {
            try {
                strings.get(it)
            } catch (ex: MissingResourceException) {
                "$$$it$$"
            }
        }.toTypedArray()

        val messageScreen = screens.filter { it is MessageScreen }.firstOrNull() as MessageScreen?
        if (messageScreen != null)
            messageScreen.addMessages(*messageStrings)
        else
            addScreen(MessageScreen(this, *messageStrings))
    }


    fun showAndiMessages(vararg messages: String) {
        val start = Node()
        messages.fold(start) {
            node, msg -> Node().apply {
                type = Node.Type.Text
                color = Color.valueOf("#008fff")
                speaker = "Andi_Player"
                text = msg
                node.next = this
            }
        }
        showDialog(start)
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
        for (screen in Array(screens)) {
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
        for (pair in screensToRemove.filter { screens.contains(it.first, true) }) {
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

    fun <T: BaseScreen> addScreen(screen: T): T{
        screensToAdd.add(screen)
        return screen
    }

    fun isScreenVisible(screen: BaseScreen): Boolean {
        return screens.contains(screen, true)
    }

    fun startNewGame() {
        Storage.SAVESTATE.clear()
        screens.filter { it is MainMenuScreen }.forEach { removeScreen(it) }
        engine.blendScreen(2.0f, Color.BLACK)

        engine.timeout(2.0f) {
            engine.entities.forEach { engine.removeEntity(it) }
            engine.timeout(0f) {
                try {
                    SceneLoader.loadScene(engine, Gdx.files.internal("scenes/scene1.scene"))
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: SceneLoader.LoadException) {
                    e.printStackTrace()
                }
            }
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
            if (keycode == Input.Keys.ENTER && Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT)) {
                if (Gdx.graphics.isFullscreen)
                    Gdx.graphics.setWindowedMode(1920, 1080)
                else for (mode in Gdx.graphics.getDisplayModes(Gdx.graphics.primaryMonitor)) {
                        if (mode.width == 1920 && mode.height == 1080) {
                            Gdx.graphics.setFullscreenMode(mode)
                            break
                        }
                    }
                return true
            }
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

class DialogHandle internal constructor(private val screen: DialogScreen, private val game: IceGame) {
    fun cancel() {
        game.removeScreen(screen)
    }
}
