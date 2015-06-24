package de.project.ice.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.project.ice.IceGame;
import de.project.ice.inventory.Inventory;
import de.project.ice.utils.Assets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static de.project.ice.config.Config.*;

public class InventoryScreen extends BaseScreenAdapter {
    private static final int ICON_ROWS = 8;
    private static final int ICON_COLUMNS = 8;
    private static final float ICON_SIZE = 64f;
    private static final float ICON_SPACE = 16f;
    private static final float VIEWPORT_SIZE = 1024f;
    private static final float MARGIN_VERTICAL = (VIEWPORT_SIZE - ICON_ROWS * (ICON_SIZE + ICON_SPACE)) * 0.5f;
    private static final float MARGIN_HORIZONTAL = (VIEWPORT_SIZE - ICON_COLUMNS * (ICON_SIZE + ICON_SPACE)) * 0.5f;
    private final Skin skin;


    private SpriteBatch batch;
    private final OrthographicCamera camera;
    private final Viewport viewport;

    public InventoryScreen(@NotNull final IceGame game) {
        super(game);

        skin = new Skin(Gdx.files.internal("ui/skin.json"));

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new FitViewport(VIEWPORT_SIZE, VIEWPORT_SIZE, camera);

        batch = new SpriteBatch();

        inputProcessor = new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {
                switch (keycode) {
                    case INVENTORY_KEY:
                        game.removeScreen(InventoryScreen.this);
                        return true;

                    case MENU_KEY:
                        return false;

                    default:
                        return true;
                }
            }

            @Override
            public boolean keyUp(int keycode) {
                return true;
            }

            @Override
            public boolean keyTyped(char character) {
                return true;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Inventory.Item item = itemAt(screenX, screenY);
                switch (button) {
                    case Input.Buttons.LEFT:
                        game.engine.controlSystem.active_item = item;
                        break;

                    case Input.Buttons.RIGHT:

                        break;
                }
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                return true;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                Vector2 pos = viewport.unproject(new Vector2(screenX, screenY));
                if ((pos.x > VIEWPORT_SIZE || pos.x < 0) && game.engine.controlSystem.active_item != null) {
                    game.removeScreen(InventoryScreen.this);
                }
                return true;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                game.setPrimaryCursor(CursorScreen.Cursor.None);
                game.setSecondaryCursor(CursorScreen.Cursor.None);
                Inventory.Item item = itemAt(screenX, screenY);
                if (item != null) {
                    game.setPrimaryCursor(CursorScreen.Cursor.Take);
                    game.setSecondaryCursor(CursorScreen.Cursor.Look);
                }
                return true;
            }

            @Override
            public boolean scrolled(int amount) {
                return true;
            }
        };
    }

    @Override
    public void resume() {
        game.setPrimaryCursor(CursorScreen.Cursor.None);
        game.setSecondaryCursor(CursorScreen.Cursor.None);
    }

    public void resize (int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void update(float delta) {
    }

    public void render () {
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        int column = 0;
        int row = 0;
        batch.draw(skin.getRegion("inventory_bg"), 0f, 0f, 1024f, 1024f);
        for (Inventory.Item item : game.inventory.items) {
            Assets.TextureRegionHolder holder = Assets.findRegion(item.getIcon());
            if (holder.data != null) {
                Vector2 pos = calcPos(row, column);
                batch.draw(holder.data, pos.x, pos.y, ICON_SIZE, ICON_SIZE);
            }
            if (++column >= ICON_COLUMNS) {
                column = 0;
                ++row;
            }
        }
        batch.end();
    }

    private Vector2 calcPos(int row, int column) {
        float x = MARGIN_HORIZONTAL + ICON_SIZE*column + ICON_SPACE*column + ICON_SPACE*0.5f;
        float y = MARGIN_VERTICAL + ICON_SIZE*row + ICON_SPACE*row + ICON_SPACE*0.5f;
        return new Vector2(x, y);
    }

    @Nullable
    private Inventory.Item itemAt(float x, float y) {
        Vector2 pos = viewport.unproject(new Vector2(x, y));
        int column = (int) ((pos.x - MARGIN_HORIZONTAL) / (ICON_SIZE + ICON_SPACE));
        int row = (int) ((pos.y - MARGIN_VERTICAL) / (ICON_SIZE + ICON_SPACE));

        if (row >= ICON_ROWS || column >= ICON_COLUMNS)
            return null;

        if(!new Rectangle(MARGIN_HORIZONTAL + ICON_SIZE*column + ICON_SPACE*column,
                MARGIN_VERTICAL + ICON_SIZE*row + ICON_SPACE*row,
                ICON_SIZE,
                ICON_SIZE).contains(pos))
            return null;

        int index = row * ICON_COLUMNS + column;
        if (index >= game.inventory.items.size)
            return null;
        else
            return game.inventory.items.get(index);
    }

    public void dispose() {
        batch.dispose();
        skin.dispose();
    }
    @Override
    public int getPriority () {
        return 900;
    }
}
