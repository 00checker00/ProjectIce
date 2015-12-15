package de.project.ice.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.project.ice.IceGame;
import de.project.ice.utils.Assets;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class CursorScreen extends BaseScreenAdapter
{
    public enum Cursor
    {
        None,
        Walk,
        Look,
        Speak,
        Take
    }

    private Pixmap cursorPixmap;
    private SpriteBatch batch;
    private TextureRegion cursor;
    private final TextureAtlas atlas;
    private final AssetManager manager;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final HashMap<Cursor, TextureRegion> cursors = new HashMap<Cursor, TextureRegion>();

    public CursorScreen(@NotNull IceGame game)
    {
        super(game);

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new ScreenViewport(camera);


        cursorPixmap = new Pixmap(32, 32, Pixmap.Format.RGBA8888);

        Gdx.graphics.newCursor(cursorPixmap, 0, 0).setSystemCursor();

        batch = new SpriteBatch();

        manager = new AssetManager();
        manager.load("ui/skin.atlas", TextureAtlas.class);
        manager.finishLoading();
        atlas = manager.get("ui/skin.atlas");
        cursor = atlas.findRegion("cursor");
        cursors.put(Cursor.None, atlas.findRegion("transparent"));
        cursors.put(Cursor.Walk, atlas.findRegion("cur_walk"));
        cursors.put(Cursor.Speak, atlas.findRegion("cur_speak"));
        cursors.put(Cursor.Look, atlas.findRegion("cur_look"));
        cursors.put(Cursor.Take, atlas.findRegion("cur_take"));
    }

    @NotNull
    public Cursor getPrimaryCursor()
    {
        return game.engine.controlSystem.primaryCursor;
    }

    public void setPrimaryCursor(@NotNull Cursor primaryCursor)
    {
        game.engine.controlSystem.primaryCursor = primaryCursor;
    }

    @NotNull
    public Cursor getSecondaryCursor()
    {
        return game.engine.controlSystem.secondaryCursor;
    }

    public void setSecondaryCursor(@NotNull Cursor secondaryCursor)
    {
        game.engine.controlSystem.secondaryCursor = secondaryCursor;
    }

    @Override
    public int getPriority()
    {
        return 10;
    }

    public void resize(int width, int height)
    {
        viewport.update(width, height, true);
    }

    @Override
    public void update(float delta)
    {
    }

    public void render()
    {
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        if (game.engine.controlSystem.hotspot_entity != null)
        {
            batch.setColor(Color.GREEN);
        }

        batch.draw(cursor, Gdx.input.getX(), viewport.getScreenHeight() - Gdx.input.getY() - cursor.getRegionHeight());
        batch.setColor(Color.WHITE);
        if (game.engine.controlSystem.active_item == null)
        {
            batch.draw(cursors.get(game.engine.controlSystem.primaryCursor), Gdx.input.getX() + 10, viewport.getScreenHeight() - Gdx.input.getY() - 32 - 10, 32, 32);
            batch.draw(cursors.get(game.engine.controlSystem.secondaryCursor), Gdx.input.getX() + 2, viewport.getScreenHeight() - Gdx.input.getY() - 16 - 2, 16, 16);
        }
        else
        {
            Assets.TextureRegionHolder item = Assets.findRegion(game.engine.controlSystem.active_item.icon);
            if (item.isValid())
            {
                batch.draw(item.data, Gdx.input.getX() + 10, viewport.getScreenHeight() - Gdx.input.getY() - 32 - 10, 32, 32);
            }
        }
        batch.end();
    }

    public void dispose()
    {
        batch.dispose();
        atlas.dispose();
        manager.dispose();
    }


}
