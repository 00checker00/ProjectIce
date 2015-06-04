package de.project.ice.utils;


import com.badlogic.gdx.InputProcessor;

/**
 * InputProcessor which just delegates all call to another InputProcessor
 * Usage: Subclass and selectively override Events your interested in
 */
public class DelegatingInputProcessor implements InputProcessor {
    private InputProcessor processor;

    public DelegatingInputProcessor(InputProcessor processor) {
        this.processor = processor;
    }

    @Override
    public boolean keyDown(int keycode) {
        return processor.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return processor.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        return processor.keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return processor.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return processor.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return processor.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return processor.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(int amount) {
        return processor.scrolled(amount);
    }
}
