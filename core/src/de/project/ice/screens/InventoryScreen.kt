package de.project.ice.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.viewport.FitViewport
import de.project.ice.IceGame
import de.project.ice.config.Config.INVENTORY_KEY
import de.project.ice.config.Config.MENU_KEY
import de.project.ice.inventory.Inventory
import de.project.ice.utils.Assets

class InventoryScreen(game: IceGame) : BaseScreenAdapter(game) {
    private val skin = Skin(Gdx.files.internal("ui/skin.json"))
    private val batch = SpriteBatch()
    private val camera = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    private val viewport = FitViewport(VIEWPORT_SIZE, VIEWPORT_SIZE, camera)
    override val inputProcessor = object : InputAdapter() {
        override fun keyDown(keycode: Int): Boolean {
            when (keycode) {
                INVENTORY_KEY -> {
                    game.removeScreen(this@InventoryScreen)
                    return true
                }

                MENU_KEY -> return false

                else -> return true
            }
        }

        override fun keyTyped(character: Char): Boolean {
            return true
        }

        override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
            val item = itemAt(screenX.toFloat(), screenY.toFloat())
            when (button) {
                Input.Buttons.LEFT -> game.engine.controlSystem.active_item = item

                Input.Buttons.RIGHT -> if (item != null) {
                    game.showMessages(game.strings.get(item.description))
                }
            }
            return true
        }

        override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
            return true
        }

        override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
            val pos = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
            if ((pos.x > VIEWPORT_SIZE || pos.x < 0) && game.engine.controlSystem.active_item != null) {
                game.removeScreen(this@InventoryScreen)
            }
            return true
        }

        override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
            game.primaryCursor = CursorScreen.Cursor.None
            game.secondaryCursor = CursorScreen.Cursor.None
            val item = itemAt(screenX.toFloat(), screenY.toFloat())
            if (item != null) {
                game.primaryCursor = CursorScreen.Cursor.Take
                game.secondaryCursor = CursorScreen.Cursor.Look
            }
            return true
        }

        override fun scrolled(amount: Int): Boolean {
            return true
        }
    }

    override fun resume() {
        game.primaryCursor = CursorScreen.Cursor.None
        game.secondaryCursor = CursorScreen.Cursor.None
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun update(delta: Float) {
    }

    override fun render() {
        viewport.apply()
        batch.projectionMatrix = camera.combined
        batch.begin()
        var column = 0
        var row = 0
        batch.draw(skin.getRegion("inventory_bg"), 0f, 0f, 1024f, 1024f)
        for (item in game.inventory.items) {
            val holder = Assets.findRegion(item.icon)
            if (holder.data != null) {
                val pos = calcPos(row, column)
                batch.draw(holder.data, pos.x, pos.y, ICON_SIZE, ICON_SIZE)
            }
            if (++column >= ICON_COLUMNS) {
                column = 0
                ++row
            }
        }
        batch.end()
    }

    private fun calcPos(row: Int, column: Int): Vector2 {
        val x = MARGIN_HORIZONTAL + ICON_SIZE * column + ICON_SPACE * column + ICON_SPACE * 0.5f
        val y = MARGIN_VERTICAL + ICON_SIZE * row + ICON_SPACE * row + ICON_SPACE * 0.5f
        return Vector2(x, y)
    }

    private fun itemAt(x: Float, y: Float): Inventory.Item? {
        val pos = viewport.unproject(Vector2(x, y))
        val column = ((pos.x - MARGIN_HORIZONTAL) / (ICON_SIZE + ICON_SPACE)).toInt()
        val row = ((pos.y - MARGIN_VERTICAL) / (ICON_SIZE + ICON_SPACE)).toInt()

        if (row >= ICON_ROWS || column >= ICON_COLUMNS) {
            return null
        }

        if (!Rectangle(MARGIN_HORIZONTAL + ICON_SIZE * column + ICON_SPACE * column,
                MARGIN_VERTICAL + ICON_SIZE * row + ICON_SPACE * row,
                ICON_SIZE,
                ICON_SIZE).contains(pos)) {
            return null
        }

        val index = row * ICON_COLUMNS + column
        if (index >= game.inventory.items.size || index < 0) {
            return null
        } else {
            return game.inventory.items.get(index)
        }
    }

    override fun dispose() {
        batch.dispose()
        skin.dispose()
    }

    override val priority: Int
        get() = 900

    companion object {
        private val ICON_ROWS = 8
        private val ICON_COLUMNS = 8
        private val ICON_SIZE = 64f
        private val ICON_SPACE = 16f
        private val VIEWPORT_SIZE = 1024f
        private val MARGIN_VERTICAL = (VIEWPORT_SIZE - ICON_ROWS * (ICON_SIZE + ICON_SPACE)) * 0.5f
        private val MARGIN_HORIZONTAL = (VIEWPORT_SIZE - ICON_COLUMNS * (ICON_SIZE + ICON_SPACE)) * 0.5f
    }
}
