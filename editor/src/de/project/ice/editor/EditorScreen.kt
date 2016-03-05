package de.project.ice.editor

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.XmlWriter
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.kotcrab.vis.ui.VisUI
import com.kotcrab.vis.ui.util.dialog.Dialogs
import com.kotcrab.vis.ui.util.dialog.InputDialogListener
import com.kotcrab.vis.ui.widget.*
import com.kotcrab.vis.ui.widget.file.FileChooser
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter
import de.project.ice.Storage
import de.project.ice.screens.BaseScreenAdapter
import de.project.ice.utils.*
import de.project.ice.utils.SceneLoader.SceneProperties
import de.project.ice.utils.SceneLoader.ScenePropertiesBuilder
import java.io.*

class EditorScreen(private val app: EditorApplication) : BaseScreenAdapter(app), EntitiesWindow.SelectionListener {
    private val pathScreen: PathScreen by lazy { PathScreen(app) }
    private val stage: Stage by lazy { Stage() }
    private val root: VisTable by lazy { VisTable(true) }
    private val menuBar: MenuBar by lazy { MenuBar() }
    private var stopPlaytest: MenuItem? = null
    private var startPlaytest: MenuItem? = null
    internal val entitiesWindow: EntitiesWindow by lazy { EntitiesWindow(app, undoRedoManager) }
    internal val componentsWindow: ComponentsWindow by lazy { ComponentsWindow(app, undoRedoManager) }
    private val undoRedoManager = app.undoManager
    private var filename: String? = null
    private var storedState: String? = null
    private var redoBtn: VisTextButton? = null
    private var undoBtn: VisTextButton? = null

    override val inputProcessor: InputProcessor = object : DelegatingInputProcessor(stage) {
        override fun keyDown(keycode: Int): Boolean {
            if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                when (keycode) {
                    Input.Keys.S -> {
                        save()
                        return true
                    }

                    Input.Keys.O -> {
                        open()
                        return true
                    }

                    Input.Keys.N -> {
                        newScene()
                        return true
                    }

                    Input.Keys.P -> {
                        if (app.isScreenVisible(pathScreen)) {
                            app.removeScreen(pathScreen, false)
                        } else {
                            app.addScreen(pathScreen)
                        }
                        return true
                    }

                    Input.Keys.Z -> {
                        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                            undoRedoManager.redo()
                        } else {
                            undoRedoManager.undo()
                        }
                        return true
                    }

                    Input.Keys.H -> {
                        hideAllWindows()
                        return true
                    }

                    Input.Keys.A -> {
                        showAllWindows()
                        return true
                    }
                }
            }
            return super.keyDown(keycode)
        }
    }

    init {
        VisUI.load()
        FileChooser.setFavoritesPrefsName("de.project.ice.editor")

        val storage = Storage.GLOBAL
        //stage.setDebugAll(true);
        stage.viewport = ScreenViewport()

        root.setFillParent(true)
        stage.addActor(root)

        root.add(menuBar.table).expandX().fillX().row()
        root.add().expand().fill()

        createMenus()

        if (storage.getBoolean("editor_pathscreen_visible", true)) {
            app.addScreen(pathScreen)
        }

        entitiesWindow.setSelectionListener(this)
        entitiesWindow.setPosition(storage.getFloat("editor_entities_x", 0f), storage.getFloat("editor_entities_y", 0f))
        entitiesWindow.setSize(storage.getFloat("editor_entities_width", 200f), storage.getFloat("editor_entities_height", 400f))
        entitiesWindow.isVisible = storage.getBoolean("editor_entities_visible", true)
        stage.addActor(entitiesWindow)

        componentsWindow.setPosition(storage.getFloat("editor_components_x", 0f), storage.getFloat("editor_components_y", 0f))
        componentsWindow.setSize(storage.getFloat("editor_components_width", 400f), storage.getFloat("editor_components_height", 400f))
        componentsWindow.isVisible = storage.getBoolean("editor_components_visible", true)
        stage.addActor(componentsWindow)

        Gdx.graphics.setWindowedMode(storage.getInteger("editor_screen_width", 800),
                storage.getInteger("editor_screen_height", 600))

        val file = Gdx.files.internal(Storage.GLOBAL.getString("editor_last_scene"))
        if (!file.name().isEmpty() && file.exists()) {
            try {
                SceneLoader.loadScene(app.engine, file.read()) assignTo app.sceneProperties
                filename = file.file().canonicalFile.absolutePath
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: SceneLoader.LoadException) {
                e.printStackTrace()
            }
        }

    }

    private fun hideAllWindows() {
        entitiesWindow.isVisible = false
        componentsWindow.isVisible = false
        game.removeScreen(pathScreen, false)
    }

    private fun showAllWindows() {
        entitiesWindow.isVisible = true
        componentsWindow.isVisible = true
        game.addScreen(pathScreen)
    }

    private fun createMenus() {
        val fileMenu = Menu("File")
        val windowMenu = Menu("Window")
        val assetsMenu = Menu("Assets")
        val testMenu = Menu("Test")
        val helpMenu = Menu("Help")

        fileMenu.addItem(MenuItem("New Scene", object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) = newScene()
        }).setShortcut("Ctrl + N"))
        fileMenu.addItem(MenuItem("Open Scene", object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) = open()
        }).setShortcut("Ctrl + O"))
        fileMenu.addItem(MenuItem("Save Scene", object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) = save(false)
        }).setShortcut("Ctrl + S"))
        fileMenu.addItem(MenuItem("Save Scene As", object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) = save(true)
        }).setShortcut("Ctrl + Shift + S"))

        fileMenu.addItem(MenuItem("Scene Properties", object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                val scenePropertiesDialog = ScenePropertiesDialog(app.sceneProperties)
                scenePropertiesDialog.addListener(object : DialogListener<SceneProperties> {
                    override fun onResult(result: SceneProperties) {
                        game.engine.soundSystem.unloadSounds()
                        game.engine.soundSystem.stopMusic()
                        game.engine.soundSystem.playMusic(result.music)

                        for (sound in result.sounds) {
                            game.engine.soundSystem.loadSound(sound)
                        }

                        reloadAssets()
                    }

                    override fun onCancel() {
                    }

                    override fun onChange(result: SceneProperties) {
                    }
                }).show(stage)
            }
        }))
        fileMenu.addItem(MenuItem("Screenshot", object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                ScreenshotFactory.saveScreenshot()
            }
        }))
        fileMenu.addSeparator()
        fileMenu.addItem(MenuItem("Quit", object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                quit()
            }
        }))

        windowMenu.addItem(MenuItem("Show/Hide Entities Window", object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                entitiesWindow.isVisible = !entitiesWindow.isVisible
            }
        }))
        windowMenu.addItem(MenuItem("Show/Hide Components Window", object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                componentsWindow.isVisible = !componentsWindow.isVisible
            }
        }))

        windowMenu.addItem(MenuItem("Show/Hide Path Screen ", object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                if (game.isScreenVisible(pathScreen)) {
                    game.removeScreen(pathScreen, false)
                } else {
                    game.addScreen(pathScreen)
                }

            }
        }).setShortcut("Ctrl + P"))


        windowMenu.addItem(MenuItem("Show All Windows", object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                showAllWindows()
            }
        }))

        windowMenu.addItem(MenuItem("Hide All Windows", object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                hideAllWindows()
            }
        }))

        assetsMenu.addItem(MenuItem("Reload Assets", object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                reloadAssets()
            }
        }))

        startPlaytest = MenuItem("Start PlayTest", object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                stopPlaytest!!.isDisabled = false
                startPlaytest!!.isDisabled = true
                storeState()
                game.resumeGame()
            }
        })
        testMenu.addItem(startPlaytest)

        stopPlaytest = MenuItem("Stop PlayTest", object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                stopPlaytest!!.isDisabled = true
                startPlaytest!!.isDisabled = false
                restoreState()
                game.pauseGame()
            }
        })
        stopPlaytest!!.isDisabled = true
        testMenu.addItem(stopPlaytest)

        helpMenu.addItem(MenuItem("About", object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                Dialogs.showOKDialog(stage, "about", "ProjectIce Editor version " + VERSION)
            }
        }))

        menuBar.addMenu(fileMenu)
        menuBar.addMenu(windowMenu)
        menuBar.addMenu(assetsMenu)
        menuBar.addMenu(testMenu)
        menuBar.addMenu(helpMenu)

        undoBtn = VisTextButton("Undo", object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                undoRedoManager.undo()
            }
        })
        menuBar.table.add<VisTextButton>(undoBtn)

        redoBtn = VisTextButton("Redo", object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                undoRedoManager.redo()
            }
        })
        menuBar.table.add<VisTextButton>(redoBtn)
    }

    private fun storeState() {
        val writer = StringWriter()
        val xml = XmlWriter(writer)
        serializeScene(xml)
        storedState = writer.toString()
        try {
            val fileWriter = FileWriter("\$stored_state$")
            fileWriter.write(storedState)
            fileWriter.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun reloadAssets() {
        Assets.clear()
        Assets.finishAll()
        for (spritesheet in app.sceneProperties.spritesheets) {
            Assets.loadAtlas(spritesheet)
        }
        Assets.finishAll()
    }

    private fun restoreState() {
        game.inventory.items.clear()
        if (storedState == null) {
            return
        }
        game.engine.removeAllEntities()

        // Update the engine to actually remove the entities
        game.engine.update(0f)

        try {
            SceneLoader.loadScene(game.engine, storedState!!)
            storedState = null
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: SceneLoader.LoadException) {
            e.printStackTrace()
        }

    }

    private fun clear() {
        filename = null
        game.engine.removeAllEntities()
        Assets.clear()

        game.pauseGame()
    }

    private fun newScene() {
        clear()
        Dialogs.showInputDialog(stage, "Scene Name", "Name", object : InputDialogListener {
            override fun finished(name: String) {
                ScenePropertiesBuilder().engine(game.engine).name(name).create() assignTo app.sceneProperties
            }

            override fun canceled() {
            }
        })
    }

    private fun open() {
        clear()

        val fileChooser = FileChooser(FileChooser.Mode.OPEN)
        fileChooser.setDirectory(FileHandle("."))
        fileChooser.selectionMode = FileChooser.SelectionMode.FILES
        fileChooser.fileFilter = FileFilter { pathname -> pathname.name.endsWith(".scene") || pathname.isDirectory }
        fileChooser.setListener(object : FileChooserAdapter() {
            override fun selected(files: com.badlogic.gdx.utils.Array<FileHandle>) {
                val file = files.first()!!
                try {
                    SceneLoader.loadScene(game.engine, file.read()) assignTo app.sceneProperties
                    filename = file.file().canonicalFile.absolutePath
                    Storage.GLOBAL.apply {
                        put("editor_last_scene", file.file().canonicalFile.absolutePath)
                        save()
                    }
                } catch (e: IOException) {
                    Dialogs.showErrorDialog(stage, "Couldn't save the scene.", e)
                } catch (e: SceneLoader.LoadException) {
                    Dialogs.showErrorDialog(stage, "Scene files is invalid", e)
                }
            }
        })
        stage.addActor(fileChooser.fadeIn())
    }

    private fun save(forceFileDialog: Boolean = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
        if (filename == null || forceFileDialog) {
            val fileChooser = FileChooser(FileChooser.Mode.SAVE)
            fileChooser.setDirectory(if (filename != null) FileHandle(filename).parent() else FileHandle("."))
            fileChooser.selectionMode = FileChooser.SelectionMode.FILES
            fileChooser.fileFilter = FileFilter { pathname -> pathname.name.endsWith(".scene") || pathname.isDirectory }
            fileChooser.setListener(object : FileChooserAdapter() {
                override fun selected(files: com.badlogic.gdx.utils.Array<FileHandle>?) {
                    try {
                        filename = files!!.first().file().canonicalFile.absolutePath
                        if (!filename!!.endsWith(".scene")) {
                            filename += ".scene"
                        }
                        save()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            })
            stage.addActor(fileChooser.fadeIn())
            return
        }

        try {
            if (storedState == null) {
                val file = File(filename)
                if (file.exists()) {
                    file.renameTo(File(file.parentFile.name + "/" + file.name + ".bak"))
                }
                val xml = XmlWriter(FileWriter(filename))
                serializeScene(xml)
            } else {
                val writer = FileWriter(filename)
                val bufferedWriter = BufferedWriter(writer)
                bufferedWriter.write(storedState)
                bufferedWriter.close()
            }
        } catch (e: IOException) {
            Dialogs.showErrorDialog(stage, "Couldn't access the file.", e)
        }

    }

    private fun serializeScene(xml: XmlWriter) {
        try {
            SceneWriter.Builder().apply {
                engine(game.engine)
                writer(xml)
                sceneName(app.sceneProperties.name)
                onloadScript(app.sceneProperties.onloadScript)
            }.create().serializeScene()

        } catch (e: IOException) {
            Dialogs.showErrorDialog(stage, "Couldn't save the scene.", e)
        }

    }


    private fun quit() {
        Gdx.app.exit()
    }

    override val priority: Int
        get() = 1

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
        Storage.GLOBAL.apply {
            put("editor_screen_width", width)
            put("editor_screen_height", height)
            save()
        }
    }

    override fun update(delta: Float) {
        stage.act(delta)
        if (filename == null) {
            Gdx.graphics.setTitle("Editor (Here be dragons)")
        } else {
            Gdx.graphics.setTitle("Editor (Here be dragons) [$filename]")
        }
        undoBtn!!.isDisabled = !undoRedoManager.canUndo()
        redoBtn!!.isDisabled = !undoRedoManager.canRedo()
    }

    override fun render() {
        stage.draw()
    }

    override fun dispose() {
        Storage.GLOBAL.apply {
            put("editor_components_x", componentsWindow.x)
            put("editor_components_y", componentsWindow.y)
            put("editor_components_width", componentsWindow.width)
            put("editor_components_height", componentsWindow.height)
            put("editor_components_visible", componentsWindow.isVisible)
            put("editor_entities_x", entitiesWindow.x)
            put("editor_entities_y", entitiesWindow.y)
            put("editor_entities_width", entitiesWindow.width)
            put("editor_entities_height", entitiesWindow.height)
            put("editor_entities_visible", entitiesWindow.isVisible)
            put("editor_pathscreen_visible", game.isScreenVisible(pathScreen))
            save()
        }
        stage.dispose()
        VisUI.dispose()
    }

    override fun selectionChanged(entity: Entity?) {
        componentsWindow.setEntity(entity)
    }

    companion object {
        private val VERSION = "0.0.1"
    }


}
