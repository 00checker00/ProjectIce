package de.project.ice.screens

import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import de.project.ice.IceGame
import de.project.ice.utils.Assets

class PapierstueckScreen(game: IceGame): BaseScreenAdapter(game) {
    private val batch = SpriteBatch()
    private val camera = OrthographicCamera(1920f, 1080f)
    private val letter: Assets.Holder.TextureRegion by lazy { Assets.findRegion("zettel") }

    override val inputProcessor = object: InputAdapter() {
        override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
            game.removeScreen(this@PapierstueckScreen)
            return true;
        }

        override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int) = true
        override fun mouseMoved(screenX: Int, screenY: Int) = true
        override fun keyTyped(character: Char) = true
        override fun scrolled(amount: Int) = true
        override fun keyUp(keycode: Int) = true
        override fun touchDragged(screenX: Int, screenY: Int, pointer: Int) = true
        override fun keyDown(keycode: Int) = true
    }

    override fun render() {
        camera.update()

        batch.projectionMatrix = camera.combined

        batch.begin()
        batch.draw(letter.data,  -(letter.data?.regionWidth?:0)/2.0f,  -(letter.data?.regionHeight?:0)/2.0f)
        batch.end()
    }

    override fun resize(width: Int, height: Int) {
        camera.viewportWidth = width.toFloat()
        camera.viewportHeight = height.toFloat()
    }

    override fun dispose() {
        batch.dispose()
    }
}