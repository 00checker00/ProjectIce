package de.project.ice.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Ellipse
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.viewport.FitViewport
import de.project.ice.IceGame
import de.project.ice.Storage
import de.project.ice.config.Config.INVENTORY_KEY
import de.project.ice.config.Config.MENU_KEY
import de.project.ice.ecs.Components
import de.project.ice.ecs.getComponents
import de.project.ice.inventory.Combinations
import de.project.ice.inventory.Inventory
import de.project.ice.inventory.Items
import de.project.ice.utils.*
import java.util.*

internal enum class Button { Load, Save, Home }
internal data class ButtonTexture(val normal: TextureRegion, val hover: TextureRegion)

class InventoryScreen(game: IceGame) : BaseScreenAdapter(game) {
    private val skin = DefaultSkin
    private val bag: TextureRegion by lazy { skin.getRegion("beutel") }
    private val bag_overlay: TextureRegion by lazy { skin.getRegion("beutel_ebene") }
    private val buttons = mapOf(
            Button.Load to ButtonTexture(skin.getRegion("load_button_none"), skin.getRegion("load_button_hover")),
            Button.Save to ButtonTexture(skin.getRegion("save_button_none"), skin.getRegion("save_button_hover")),
            Button.Home to ButtonTexture(skin.getRegion("home_button_none"), skin.getRegion("home_button_hover"))
    )
    private val batch = SpriteBatch()
    private val camera = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    private val viewport = FitViewport(bag.regionWidth.toFloat(), bag.regionHeight.toFloat(), camera)
    private val itemPositions = ObjectMap<Inventory.Item, Vector2>()

    private var draggedItem: Inventory.Item? = null
    private var dragStart = Vector2()
    private var dragOffset = Vector2()
    private var dragged = false

    init {
        game.primaryCursor = CursorScreen.Cursor.None
        game.secondaryCursor = CursorScreen.Cursor.None
        game.cursorText = ""

        Storage.SAVESTATE.getString(ITEM_POSITION_STORAGE_KEY, "")
                .split(';')
                .filter { it.isNotBlank() }
                .map { it.split(':') }
                .map { Pair(Items[it[0]], Vector2(it[1].toFloat(), it[2].toFloat())) }
                .forEach { itemPositions.put(it.first, it.second) }

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
            val pos = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()));
            val item = getItemAtPos(pos)
            when (button) {
                Input.Buttons.LEFT -> {

                    if(Circle(BUTTON_POSITIONS[Button.Save], BUTTON_SIZE).contains(pos) ) {
                        if(game.blockSaving) {
                            game.showMessages("general_save_blocked")
                        } else {
                            val xmlState = SceneWriter.serializeToString(game.engine, game.engine.sceneProperties!!)

                            Storage.SAVESTATE.put("__SAVESTATE__", xmlState)
                            game.removeScreen(this@InventoryScreen)
                            game.showMessages("general_save_success")
                        }


                    } else if(Circle(BUTTON_POSITIONS[Button.Load], BUTTON_SIZE).contains(pos)) {
                        if(game.blockSaving) {
                            game.showMessages("general_load_blocked")
                        } else {
                            if (Storage.SAVESTATE.hasKey("__SAVESTATE__")) {

                                game.removeScreen(this@InventoryScreen)
                                game.engine.blendScreen(1.0f, Color.BLACK)
                                game.engine.timeout(1.0f) {
                                    val xmlState = Storage.SAVESTATE.getString("__SAVESTATE__")

                                    game.engine.entities.forEach {
                                        game.engine.removeEntity(it)
                                    }

                                    game.engine.timeout(0f) {
                                        game.engine.timeout(0f) {
                                            game.engine.blendScreen(0.0f, Color.BLACK)
                                            game.engine.timeout(0.5f) {
                                                game.engine.blendScreen(1.0f, Color.WHITE, Color.BLACK)
                                            }

                                            SceneLoader.loadScene(game.engine, xmlState)
                                            game.showMessages("general_load_success")
                                        }
                                    }
                                }
                            } else {
                                game.showMessages("general_load_nosave")
                            }
                        }

                    } else {
                        var active_item = game.engine.controlSystem.active_item;

                        when {
                            active_item != null && item != null -> if (Combinations.canCombine(active_item, item)) {
                                Combinations.combine(game, active_item, item)
                                game.engine.controlSystem.active_item = null
                            }
                            active_item != null && item == null -> {
                                game.engine.controlSystem.active_item = null
                                itemPositions.put(active_item, pos + dragOffset)
                            }
                            item != null -> {
                                dragOffset = getItemPosition(item) - pos
                                draggedItem = item
                                dragStart = pos
                                itemPositions.put(draggedItem, pos + dragOffset)
                                dragged = false
                            }
                        }
                    }
                }

                Input.Buttons.RIGHT -> if (item != null) {
                    game.showMessages(item.description)
                }
            }
            return true
        }

        override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
            if (!dragged) {
                game.engine.controlSystem.active_item = draggedItem
                draggedItem = null
            }
            return true
        }

        override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
            val pos = viewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
            if (dragged || (draggedItem != null && pos.dst(dragStart) > MIN_DRAG_DIST)) {
                if (isPositionValid(pos))
                    itemPositions.put(draggedItem, pos + dragOffset)
                dragged = true
            }
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
            var position: Vector2
            do {
                position = Vector2 (
                        VALID_REGION.x - VALID_REGION.width/2 + rng.nextFloat() * VALID_REGION.width,
                        VALID_REGION.y - VALID_REGION.height/2 + rng.nextFloat() * VALID_REGION.height
                )
            } while (!isPositionValid(position))
            itemPositions.put(item, position)

            position
        }
    }

    fun getItemAtPos(pos: Vector2): Inventory.Item? {
        var items = game.inventory.items
                .map { Pair(it, getItemPosition(it)) }
                .filter { Rectangle(it.second.x, it.second.y, ICON_SIZE, ICON_SIZE).contains(pos) }
        return items.lastOrNull()?.first
    }

    private fun isPositionValid(pos: Vector2): Boolean {
        return  VALID_REGION.contains( pos.x, pos.y)
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
            if (item == game.engine.controlSystem.active_item)
                continue
        }
        for (item in game.inventory.items) {

            val holder = Assets.findRegion(item.icon)
            if (holder.data != null) {

                val pos = getItemPosition(item)
                batch.draw(holder.data, pos.x, pos.y, ICON_SIZE, ICON_SIZE)
            }
        }
        batch.draw(bag_overlay, 0f, 0f, bag.regionWidth.toFloat(), bag.regionHeight.toFloat())

        val mousePos = viewport.unproject(Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat().toFloat()))
        for (button in Button.values()) {
            val center = BUTTON_POSITIONS[button]!!
            val region = Circle(center, BUTTON_SIZE)
            val texture = if (region.contains(mousePos)) buttons[button]!!.hover else buttons[button]!!.normal
            batch.draw(texture, center.x - BUTTON_SIZE/2, center.y - BUTTON_SIZE/2, BUTTON_SIZE, BUTTON_SIZE)
        }

        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        val sb = StringBuilder()
        for (item in itemPositions) {
            sb.append("${item.key.name}:${item.value.x}:${item.value.y};")
        }
        Storage.SAVESTATE.put(ITEM_POSITION_STORAGE_KEY, sb.toString())
    }

    override val priority: Int
        get() = 900

    companion object {
        private val ICON_SIZE = 128f
        private val MIN_DRAG_DIST = 0.2f;
        private val ITEM_POSITION_STORAGE_KEY = "__INVENTORY_SCREEN_ITEM_POSITIONS__"
        private val VALID_REGION = Ellipse(555f, 485f, 400f, 280f)
        private val BUTTON_POSITIONS = mapOf(
                Button.Home to Vector2(1005f, 486f),
                Button.Load to Vector2(254f, 536f),
                Button.Save to Vector2(180f, 282f)
        )
        private val BUTTON_SIZE = 125f
    }
}
