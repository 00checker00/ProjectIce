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
    private val atlas: TextureAtlas
    private val manager: AssetManager
    private val camera: OrthographicCamera
    private val viewport: Viewport
    private val cursors = HashMap<Cursor, TextureRegion>()
    private val font = FreeTypeFontGenerator(Gdx.files.internal("ui/Minecraft.ttf")).let {
        val params = FreeTypeFontGenerator.FreeTypeFontParameter().apply {
            size = 16
            color = Color.WHITE
            borderWidth = 3f
            borderColor = Color.BLACK
        }

        val font = it.generateFont(params)
        it.dispose()

        font
    }

    init {

        camera = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        viewport = ScreenViewport(camera)


        cursorPixmap = Pixmap(32, 32, Pixmap.Format.RGBA8888)


        Gdx.graphics.setCursor(Gdx.graphics.newCursor(cursorPixmap, 0, 0))


        batch = SpriteBatch()

        manager = AssetManager()
        manager.load("ui/skin.atlas", TextureAtlas::class.java)
        manager.finishLoading()
        atlas = manager.get<TextureAtlas>("ui/skin.atlas")
        cursor_normal = atlas.findRegion("cursor_normal")
        cursor_double = atlas.findRegion("cursor_double")
        cursors.put(Cursor.None, atlas.findRegion("transparent"))
        cursors.put(Cursor.Walk, atlas.findRegion("cursor_walk"))
        cursors.put(Cursor.Speak, atlas.findRegion("cursor_talk"))
        cursors.put(Cursor.Look, atlas.findRegion("cursor_look"))
        cursors.put(Cursor.Take, atlas.findRegion("cursor_take"))
        cursors.put(Cursor.Use, atlas.findRegion("cursor_take"))
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
        val size = cursor_normal.regionWidth.toFloat()


        var base_img = if (item == null && secondary == Cursor.None) {
            cursor_normal
        } else {
            cursor_double
        }

        val x = Gdx.input.x.toFloat()
        val y = viewport.screenHeight - Gdx.input.y.toFloat() - base_img.regionHeight.toFloat()

        batch.draw(base_img, x - CURSOR_ORIGIN_X, y + CURSOR_ORIGIN_Y)

        if (item == null) {
            if (game.engine.controlSystem.secondaryCursor != Cursor.None) {
                batch.draw(cursors[primary], x- CURSOR_PRIMARY_X, y - CURSOR_PRIMARY_Y)
                batch.draw(cursors[secondary], x-CURSOR_SECONDARY_X, y - CURSOR_SECONDARY_Y)
            } else {
                batch.draw(cursors[primary], x, y)
            }
        } else {
            val icon = Assets.findRegion(item.icon)
            if (icon.isValid) {
                batch.draw(icon.data, x, y, size, size)
            }
        }


        if (hotspot != null) {
            font.draw(batch, hotspot.id, x, y)
        }

        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        atlas.dispose()
        manager.dispose()
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
