package de.project.ice.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
import de.project.ice.IceGame
import de.project.ice.utils.Assets
import de.project.ice.utils.DefaultSkin
import sun.font.TrueTypeFont
import java.util.*

class CursorScreen(game: IceGame) : BaseScreenAdapter(game) {
    enum class Cursor {
        None,
        Walk,
        Look,
        Speak,
        Take,
        Use
    }

    private val cursorPixmap: Pixmap
    private val batch: SpriteBatch
    private val cursor_normal: TextureRegion
    private val cursor_double: TextureRegion
    private val camera: OrthographicCamera
    private val viewport: Viewport
    private val cursors = HashMap<Cursor, TextureRegion>()
    private val skin = DefaultSkin
    private val font = skin.getFont("cursor")
    var cursorScale = 0.6f

    init {

        camera = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        viewport = ScreenViewport(camera)


        cursorPixmap = Pixmap(32, 32, Pixmap.Format.RGBA8888)


        Gdx.graphics.setCursor(Gdx.graphics.newCursor(cursorPixmap, 0, 0))


        batch = SpriteBatch()

        cursor_normal = skin.getRegion("cursor_normal")
        cursor_double = skin.getRegion("cursor_double")
        cursors.put(Cursor.None, skin.getRegion("transparent"))
        cursors.put(Cursor.Walk, skin.getRegion("cursor_walk"))
        cursors.put(Cursor.Speak, skin.getRegion("cursor_talk"))
        cursors.put(Cursor.Look, skin.getRegion("cursor_look"))
        cursors.put(Cursor.Take, skin.getRegion("cursor_take"))
        cursors.put(Cursor.Use, skin.getRegion("cursor_use"))
    }

    var primaryCursor: Cursor
        get() = game.engine.controlSystem.primaryCursor
        set(primaryCursor) {
            game.engine.controlSystem.primaryCursor = primaryCursor
        }

    var secondaryCursor: Cursor
        get() = game.engine.controlSystem.secondaryCursor
        set(secondaryCursor) {
            game.engine.controlSystem.secondaryCursor = secondaryCursor
        }

    var cursorText: String
        get() = game.engine.controlSystem.cursorText
        set(cursorText) {
            game.engine.controlSystem.cursorText = cursorText
        }


    override val priority: Int
        get() = 10

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun update(delta: Float) {
    }

    override fun render() {
        viewport.apply()
        batch.projectionMatrix = camera.combined
        batch.begin()

        val hotspot = game.engine.controlSystem.active_hotspot
        val primary = game.engine.controlSystem.primaryCursor
        val secondary = game.engine.controlSystem.secondaryCursor
        val item = game.engine.controlSystem.active_item

        var base_img = if (item == null && secondary == Cursor.None) {
            cursor_normal
        } else {
            cursor_double
        }

        val size = base_img.regionWidth.toFloat() * cursorScale
        val origin_x = CURSOR_ORIGIN_X * cursorScale
        val origin_y = CURSOR_ORIGIN_Y * cursorScale
        val primary_x = CURSOR_PRIMARY_X * cursorScale
        val primary_y = CURSOR_PRIMARY_Y * cursorScale
        val secondary_x = CURSOR_SECONDARY_X * cursorScale
        val secondary_y = CURSOR_SECONDARY_Y * cursorScale

        val x = Gdx.input.x.toFloat()
        val y = viewport.screenHeight - Gdx.input.y.toFloat() - size

        batch.draw(base_img, x - origin_x, y + origin_y, size, size)

        if (item == null) {
            if (game.engine.controlSystem.secondaryCursor != Cursor.None) {
                batch.draw(cursors[primary], x- primary_x, y - primary_y, size, size)
                batch.draw(cursors[secondary], x+secondary_x, y - secondary_y, size, size)
            } else {
                batch.draw(cursors[primary], x, y, size, size)
            }
        } else {
            val icon = Assets.findRegion(item.icon)
            if (icon.isValid) {
                batch.draw(icon.data, x, y, size, size)
            }
        }

        font.draw(batch, cursorText, x + size/2, y + size + font.lineHeight)


        batch.end()
    }

    override fun dispose() {
        batch.dispose()
    }

    companion object {
        private val CURSOR_ORIGIN_X = 23
        private val CURSOR_ORIGIN_Y = 19
        private val CURSOR_PRIMARY_X = 20
        private val CURSOR_PRIMARY_Y = 60
        private val CURSOR_SECONDARY_X = 75
        private val CURSOR_SECONDARY_Y = 11

    }

}
