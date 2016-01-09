package de.project.ice.utils


import com.badlogic.gdx.InputProcessor

/**
 * InputProcessor which just delegates all call to another InputProcessor
 * Usage: Subclass and selectively override Events your interested in
 */
open class DelegatingInputProcessor(protected var processor: InputProcessor) : InputProcessor {
    override fun keyDown(keycode: Int): Boolean = processor.keyDown(keycode)
    override fun keyUp(keycode: Int): Boolean = processor.keyUp(keycode)
    override fun keyTyped(character: Char): Boolean = processor.keyTyped(character)
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = processor.touchDown(screenX, screenY, pointer, button)
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = processor.touchUp(screenX, screenY, pointer, button)
    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean = processor.touchDragged(screenX, screenY, pointer)
    override fun mouseMoved(screenX: Int, screenY: Int): Boolean = processor.mouseMoved(screenX, screenY)
    override fun scrolled(amount: Int): Boolean = processor.scrolled(amount)
}
