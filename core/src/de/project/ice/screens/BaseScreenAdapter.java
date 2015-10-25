package de.project.ice.screens;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import de.project.ice.IceGame;
import org.jetbrains.annotations.NotNull;

public abstract class BaseScreenAdapter implements BaseScreen
{
    @NotNull
    protected final IceGame game;
    @NotNull
    protected InputProcessor inputProcessor;

    public BaseScreenAdapter(@NotNull IceGame game)
    {
        this.game = game;
        inputProcessor = new InputAdapter();
    }

    /**
     * {@inheritDoc}
     *
     * @return The display priority of the screen, base implementation returns 0
     */
    @Override
    public int getPriority()
    {
        return 0;
    }

    @Override
    @NotNull
    public IceGame getGame()
    {
        return game;
    }

    @Override
    public void show()
    {
    }

    @Override
    public void update(float delta)
    {
    }

    @Override
    public void render()
    {
    }

    @Override
    public void resize(int width, int height)
    {
    }

    @Override
    public void pause()
    {
    }

    @Override
    public void resume()
    {
    }

    @Override
    public void hide()
    {
    }

    @Override
    public void dispose()
    {
    }

    /**
     * {@inheritDoc}
     *
     * @return The InputProcessor for this screen, base implementation returns an empty InputProcessor
     */
    @NotNull
    @Override
    public InputProcessor getInputProcessor()
    {
        return inputProcessor;
    }
}
