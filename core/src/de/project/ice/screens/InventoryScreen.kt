package de.project.ice.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.viewport.FitViewport
import de.project.ice.IceGame
import de.project.ice.config.Config.INVENTORY_KEY
import de.project.ice.config.Config.MENU_KEY
import de.project.ice.inventory.Combinations
import de.project.ice.inventory.Inventory
import de.project.ice.utils.Assets
import de.project.ice.utils.DefaultSkin
import java.util.*

class InventoryScreen(game: IceGame) : BaseScreenAdapter(game) {
    private val skin = DefaultSkin
    private val bag: TextureRegion by lazy { skin.getRegion("beutel") }
    private val batch = SpriteBatch()
    private val camera = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    private val viewport = FitViewport(bag.regionWidth.toFloat(), bag.regionHeight.toFloat(), camera)
    private val itemPositions = ObjectMap<Inventory.Item, Vector2>()

    init {
        game.primaryCursor = CursorScreen.Cursor.None
        game.secondaryCursor = CursorScreen.Cursor.None
        game.cursorText = ""
    }

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
            val item: Inventory.Item? = getItemAtPos(viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat())))
            when (button) {
                Input.Buttons.LEFT -> {
                    var active_item = game.engine.controlSystem.active_item;
                    if ( active_item != null && item != null) {
                      if (Combinations.canCombine(active_item, item)) {
                          Combinations.combine(game, active_item, item)
                          game.engine.controlSystem.active_item = null

                      }
                    } else {
                        game.engine.controlSystem.active_item = item
                    }
                }

                Input.Buttons.RIGHT -> if (item != null) {
                    game.showMessages(item.description)
                }
            }
            return true
        }

        override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
            return true
        }

        override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
            val pos = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
            //if ((pos.x > VIEWPORT_SIZE || pos.x < 0) && game.engine.controlSystem.active_item != null) {
            //    game.removeScreen(this@InventoryScreen)
            //}
            return true
        }

        override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
            game.primaryCursor = CursorScreen.Cursor.None
            game.secondaryCursor = CursorScreen.Cursor.None
            val item = getItemAtPos(viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat())))
            if (item != null) {
                game.primaryCursor = CursorScreen.Cursor.Take
                game.secondaryCursor = CursorScreen.Cursor.Look
                game.cursorText = item.name
            } else {
                game.primaryCursor = CursorScreen.Cursor.None
                game.secondaryCursor = CursorScreen.Cursor.None
                game.cursorText = ""
            }
            return true
        }

        override fun scrolled(amount: Int): Boolean {
            return true
        }
    }

    fun getItemPosition(item: Inventory.Item): Vector2 {
        return itemPositions.get(item) ?: let {
            val rng = Random(item.name.hashCode().toLong())
            Vector2 (
                    rng.nextFloat()*bag.regionWidth,
                    rng.nextFloat()*bag.regionHeight
            ).apply { itemPositions.put(item, this) }
        }
    }

    fun getItemAtPos(pos: Vector2): Inventory.Item? {
        var items = game.inventory.items
                .map { Pair(it, getItemPosition(it)) }
                .filter { Rectangle(it.second.x, it.second.y, ICON_SIZE, ICON_SIZE).contains(pos) }
        return items.lastOrNull()?.first
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

        batch.draw(bag, 0f, 0f, bag.regionWidth.toFloat(), bag.regionHeight.toFloat())

        for (item in game.inventory.items) {

            val holder = Assets.findRegion(item.icon)
            if (holder.data != null) {
                val pos = getItemPosition(item)
                batch.draw(holder.data, pos.x, pos.y, ICON_SIZE, ICON_SIZE)
            }
        }

        batch.end()
    }

    override fun dispose() {
        batch.dispose()
    }

    override val priority: Int
        get() = 900

    companion object {
        private val ICON_SIZE = 64f
    }
}
