package de.project.ice.screens

import com.badlogic.gdx.InputProcessor
import de.project.ice.IceGame
import de.project.ice.config.Config.INVENTORY_KEY
import de.project.ice.config.Config.MENU_KEY
import de.project.ice.ecs.IceEngine
import de.project.ice.utils.DelegatingBlockingInputProcessor

/**
 * Input Handling here...
 */
class GameScreen(game: IceGame, private val engine: IceEngine) : BaseScreenAdapter(game) {
    private val SystemsToPause = arrayOf(
            engine.cameraSystem,
            engine.animationSystem,
            engine.controlSystem,
            engine.movementSystem,
            engine.scriptingSystem,
            engine.breathSystem,
            engine.timeoutSystem,
            engine.andiSystem
    )

    override val inputProcessor: InputProcessor = object : DelegatingBlockingInputProcessor(engine.controlSystem) {
        override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
            super.mouseMoved(screenX, screenY)
            game.primaryCursor = engine.controlSystem.primaryCursor
            game.secondaryCursor = engine.controlSystem.secondaryCursor
            return true
        }

        override fun keyDown(keycode: Int): Boolean {
            if (game.blockInteraction)
                return false

            when (keycode) {
                MENU_KEY, INVENTORY_KEY -> {
                    this@GameScreen.game.addScreen(InventoryScreen(this@GameScreen.game))
                    return true
                }

                else -> return super.keyDown(keycode)
            }
        }
    }

    override fun update(delta: Float) {
        engine.update(delta)
    }

    override fun resume() {
    }

    override fun pause() {
    }

    fun pauseGame() {
        for (system in SystemsToPause) {
            system.setProcessing(false)
        }
    }

    fun resumeGame() {
        for (system in SystemsToPause) {
            system.setProcessing(true)
        }
    }

    override val priority: Int
        get() = 1000
}
