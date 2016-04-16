package de.project.ice.screens

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import de.project.ice.IceGame
import de.project.ice.utils.DelegatingBlockingInputProcessor

class PauseScreen(game: IceGame) : MainMenuScreen(game) {
    override val inputProcessor: InputProcessor
        get() {
            return object : DelegatingBlockingInputProcessor(super.inputProcessor) {
                override fun keyDown(keycode: Int): Boolean {
                    if (keycode == Input.Keys.ESCAPE) {
                        this@PauseScreen.game.removeScreen(this@PauseScreen)
                        return true
                    }
                    return super.keyDown(keycode)
                }
            }
        }

    init {
        createMenuButtonAfter(ButtonProperties(BUTTON_CONTINUE_ID), "Continue", object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                this@PauseScreen.game.removeScreen(this@PauseScreen)
                return true
            }
        }, BUTTON_SETTINGS_ID)
    }

    override val priority: Int
        get() = 500

    companion object {
        val BUTTON_CONTINUE_ID = "BTN_CONTINUE"
    }
}
