package de.project.ice.editor


import com.badlogic.gdx.Gdx
import de.project.ice.IceGame
import de.project.ice.editor.undoredo.UndoRedoManager
import de.project.ice.utils.SceneLoader

class EditorApplication : IceGame() {
    val undoManager = UndoRedoManager()
    val sceneProperties: SceneLoader.SceneProperties by lazy {
        SceneLoader.ScenePropertiesBuilder().engine(engine).create()
    }

    override fun init() {
        Gdx.input.inputProcessor = inputProcessor
        pauseGame()
        addScreen(EditGameScreen(this))
        addScreen(EditorScreen(this))
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(null, 0, 0))
    }


}
