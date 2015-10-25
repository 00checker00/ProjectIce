package de.project.ice.utils;


import com.badlogic.gdx.InputProcessor;

/**
 * InputProcessor which delegates all call to another InputProcessor
 * and sets all events to handled => They won't be processed any further
 * Usage: Subclass and selectively override Events your interested in
 */
public class DelegatingBlockingInputProcessor implements InputProcessor
{
    private InputProcessor processor;

    public DelegatingBlockingInputProcessor(InputProcessor processor)
    {
        this.processor = processor;
    }

    @Override
    public boolean keyDown(int keycode)
    {
        processor.keyDown(keycode);
        return true;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        processor.keyUp(keycode);
        return true;
    }

    @Override
    public boolean keyTyped(char character)
    {
        processor.keyTyped(character);
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        processor.touchDown(screenX, screenY, pointer, button);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        processor.touchUp(screenX, screenY, pointer, button);
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        processor.touchDragged(screenX, screenY, pointer);
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        processor.mouseMoved(screenX, screenY);
        return true;
    }

    @Override
    public boolean scrolled(int amount)
    {
        processor.scrolled(amount);
        return true;
    }
}
