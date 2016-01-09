package de.project.ice.screens

import com.badlogic.gdx.InputProcessor
import de.project.ice.IceGame

/**
 *
 *
 * Represents one of many application screens, such as a main menu, a settings menu, the game screen and so on.
 *
 *
 *
 * Note that [.dispose] is not called automatically.
 *

 * @see Game
 */
interface BaseScreen {
    /**
     * The priority of the screen is used to calculate the order in which the screens are drawn.
     * 0 meaning it's the topmost screen, Integer.MAX_VALUE will be behind every other screen.
     * When two screens have the same priority, the one added first will be behind the one added later

     * @return The display priority of the screen
     */
    val priority: Int

    /**
     * Get the game to which this screen belongs

     * @return the game to which this screen belongs
     */
    val game: IceGame

    /**
     * Called when this screen becomes the current screen for a [Game].
     */
    fun show()

    /**
     * Called when the screen should update itself.

     * @param delta The time in seconds since the last render.
     */
    fun update(delta: Float)

    /**
     * Called when the screen should render itself.
     */
    fun render()

    /**
     * @see ApplicationListener.resize
     */
    fun resize(width: Int, height: Int)

    /**
     * @see ApplicationListener.pause
     */
    fun pause()

    /**
     * @see ApplicationListener.resume
     */
    fun resume()

    /**
     * Called when this screen is no longer the current screen for a [Game].
     */
    fun hide()

    /**
     * Called when this screen should release all resources.
     */
    fun dispose()

    /**
     * Get the InputProcessor for this screen
     * @return The InputProcessor for this screen
     */
    public val inputProcessor: InputProcessor
}
