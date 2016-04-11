package de.project.ice.editor


import com.badlogic.ashley.core.Entity
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
        val editGameScreen = EditGameScreen(this)
        addScreen(editGameScreen)
        addScreen(object: EditorScreen(this@EditorApplication) {
            override fun selectionChanged(entity: Entity?) {
                super.selectionChanged(entity)
                editGameScreen.selectedEntity = entity
            }
        })
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(null, 0, 0))
    }


}
