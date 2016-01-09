package de.project.ice.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.viewport.ScreenViewport
import de.project.ice.IceGame
import de.project.ice.utils.ColorDrawable
import de.project.ice.utils.DelegatingBlockingInputProcessor


class MessageScreen(game: IceGame, vararg messages: String) : BaseScreenAdapter(game) {
    private val messages: Array<String>

    private val stage = Stage()
    private val skin = Skin(Gdx.files.internal("ui/skin.json"))
    private val root = Table(skin)
    private val messageLabel: Label
    override val inputProcessor: InputProcessor = object : DelegatingBlockingInputProcessor(stage) {
        override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
            showNextMessage()
            return true
        }
    }

    init {

        this.messages = Array(messages)
        this.messages.reverse()

        stage.viewport = ScreenViewport()

        stage.addActor(root)

        root.setFillParent(true)
        root.background = ColorDrawable(Color(0f, 0f, 0f, 0.4f), true)

        root.row().expand()
        root.add("")

        messageLabel = Label(null, skin)
        root.row().expandX()
        root.add(messageLabel)

        root.row().expand()
        root.add("")

        showNextMessage()
    }

    private fun showNextMessage() {
        if (messages.size == 0) {
            game.removeScreen(this)
            return
        }

        messageLabel.setText(messages.pop())
    }

    override val priority: Int
        get() = 500

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun update(delta: Float) {
        stage.act(delta)
    }

    override fun render() {
        stage.viewport.apply()
        stage.draw()
    }

    override fun dispose() {
        stage.dispose()
    }
}
