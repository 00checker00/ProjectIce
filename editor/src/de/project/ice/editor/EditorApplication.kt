package de.project.ice.editor


import com.badlogic.gdx.Gdx
import de.project.ice.IceGame

class EditorApplication : IceGame() {
    override fun init() {
        Gdx.input.inputProcessor = inputProcessor
        pauseGame()
        addScreen(EditGameScreen(this))
        addScreen(EditorScreen(this))
        Gdx.graphics.newCursor(null, 0, 0).setSystemCursor()
    }
}
