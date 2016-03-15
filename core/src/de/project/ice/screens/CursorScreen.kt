package de.project.ice.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
import de.project.ice.IceGame
import de.project.ice.utils.Assets
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
    private val cursor: TextureRegion
    private val atlas: TextureAtlas
    private val manager: AssetManager
    private val camera: OrthographicCamera
    private val viewport: Viewport
    private val cursors = HashMap<Cursor, TextureRegion>()

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
        cursor = atlas.findRegion("cursor")
        cursors.put(Cursor.None, atlas.findRegion("transparent"))
        cursors.put(Cursor.Walk, atlas.findRegion("cur_walk"))
        cursors.put(Cursor.Speak, atlas.findRegion("cur_speak"))
        cursors.put(Cursor.Look, atlas.findRegion("cur_look"))
        cursors.put(Cursor.Take, atlas.findRegion("cur_take"))
        cursors.put(Cursor.Use, atlas.findRegion("cur_take"))
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

        if (game.engine.controlSystem.hotspot_entity != null) {
            batch.color = Color.GREEN
        }

        batch.draw(cursor, Gdx.input.x.toFloat(), viewport.screenHeight - Gdx.input.y - cursor.regionHeight.toFloat())
        batch.color = Color.WHITE
        if (game.engine.controlSystem.active_item == null) {
            batch.draw(cursors[game.engine.controlSystem.primaryCursor], (Gdx.input.x + 10).toFloat(), viewport.screenHeight - Gdx.input.y - 32 - 10.toFloat(), 32f, 32f)
            batch.draw(cursors[game.engine.controlSystem.secondaryCursor], (Gdx.input.x + 2).toFloat(), viewport.screenHeight - Gdx.input.y - 16 - 2.toFloat(), 16f, 16f)
        } else {
            val item = Assets.findRegion(game.engine.controlSystem.active_item!!.icon)
            if (item.isValid) {
                batch.draw(item.data, (Gdx.input.x + 10).toFloat(), viewport.screenHeight - Gdx.input.y - 32 - 10.toFloat(), 32f, 32f)
            }
        }
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        atlas.dispose()
        manager.dispose()
    }


}
