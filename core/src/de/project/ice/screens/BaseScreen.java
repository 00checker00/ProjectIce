package de.project.ice.screens;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputProcessor;
import de.project.ice.IceGame;
import org.jetbrains.annotations.NotNull;

/**
 * <p>
 * Represents one of many application screens, such as a main menu, a settings menu, the game screen and so on.
 * </p>
 * <p>
 * Note that {@link #dispose()} is not called automatically.
 * </p>
 *
 * @see Game
 */
public interface BaseScreen
{
    /**
     * The priority of the screen is used to calculate the order in which the screens are drawn.<br>
     * 0 meaning it's the topmost screen, Integer.MAX_VALUE will be behind every other screen.<br>
     * When two screens have the same priority, the one added first will be behind the one added later
     *
     * @return The display priority of the screen
     */
    int getPriority();

    /**
     * Get the game to which this screen belongs
     *
     * @return the game to which this screen belongs
     */
    IceGame getGame();

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    void show();

    /**
     * Called when the screen should update itself.
     *
     * @param delta The time in seconds since the last render.
     */
    void update(float delta);

    /**
     * Called when the screen should render itself.
     */
    void render();

    /**
     * @see ApplicationListener#resize(int, int)
     */
    void resize(int width, int height);

    /**
     * @see ApplicationListener#pause()
     */
    void pause();

    /**
     * @see ApplicationListener#resume()
     */
    void resume();

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    void hide();

    /**
     * Called when this screen should release all resources.
     */
    void dispose();

    /**
     * Get the InputProcessor for this screen
     *
     * @return The InputProcessor for this screen
     */
    @NotNull
    InputProcessor getInputProcessor();
}
