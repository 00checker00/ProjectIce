package de.project.ice;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import de.project.ice.dialog.Dialog;
import de.project.ice.ecs.IceEngine;
import de.project.ice.hotspot.HotspotManager;
import de.project.ice.inventory.Inventory;
import de.project.ice.screens.*;
import de.project.ice.scripting.ScriptManager;
import de.project.ice.scripting.scripts.Scene01_Load;
import de.project.ice.utils.Assets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IceGame extends ApplicationAdapter {
    private final Array<BaseScreen> screens = new Array<BaseScreen>();
    private final Array<BaseScreen> screensToAdd  = new Array<BaseScreen>();
    private final Array<BaseScreen> screensToRemove  = new Array<BaseScreen>();
    public IceEngine engine;
    protected GameScreen gameScreen = null;
    @NotNull
    protected CursorScreen cursorScreen ;
    @NotNull
    public ScriptManager scriptManager;
    @NotNull
    public Inventory inventory;
    @NotNull
    public HotspotManager hotspotManager;

    public IceGame() {
    }

    @Override
    public void create () {
        inventory = new Inventory(this);
        hotspotManager = new HotspotManager(this);
        engine = new IceEngine(this);
        scriptManager = new ScriptManager(engine);

        gameScreen = new GameScreen(this, engine);
        addScreen(gameScreen);
        cursorScreen = new CursorScreen(this);
        addScreen(cursorScreen);
        init();

    }

    protected void init() {
        addScreen(new MainMenuScreen(this));
        Gdx.input.setInputProcessor(new InputMultiplexer());
    }

    public void pauseGame() {
        if (gameScreen != null)
            gameScreen.pauseGame();
    }

    public void resumeGame() {
        if (gameScreen != null)
            gameScreen.resumeGame();
    }

    public void showDialog(String dialog) {
       addScreen(new DialogScreen(this, Dialog.load(Gdx.files.internal("dialog/" + dialog + ".dlz"))));
    }

    @Override
    public void dispose () {
        for (BaseScreen screen : screens) {
            screen.hide();
            screen.dispose();
        }
        screens.clear();
    }

    @Override
    public void pause () {
        if (screens.size > 0) {
            screens.peek().pause();
        }
    }

    @Override
    public void resume () {
        if (screens.size > 0) {
            screens.peek().resume();
        }
    }

    @NotNull
    public CursorScreen.Cursor getPrimaryCursor() {
        return cursorScreen.getPrimaryCursor();
    }

    public void setPrimaryCursor(@NotNull CursorScreen.Cursor primaryCursor) {
        cursorScreen.setPrimaryCursor(primaryCursor);
    }

    @NotNull
    public CursorScreen.Cursor getSecondaryCursor() {
        return cursorScreen.getSecondaryCursor();
    }

    public void setSecondaryCursor(@NotNull CursorScreen.Cursor secondaryCursor) {
        cursorScreen.setSecondaryCursor(secondaryCursor);
    }

    @Override
    public void render () {
        Assets.update();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        float delta = Gdx.graphics.getDeltaTime();
        for (BaseScreen screen : screens) {
            screen.update(delta);
        }
        for (BaseScreen screen : screens) {
            screen.render();
        }
        for (BaseScreen screen : screensToAdd) {
            int index = -1;
            for (int i = 0; i < screens.size; i++) {
                if (screens.get(i).getPriority() < screen.getPriority()) {
                    index = i;
                    break;
                }
            }
            if (index == -1) {
                if (screens.size > 0)
                    screens.peek().pause();
                screens.add(screen);
                screen.show();
            } else {
                screens.insert(index, screen);
            }
            screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        screensToAdd.clear();
        for (BaseScreen screen : screensToRemove) {
            if (screen == screens.peek()) {
                screens.pop();
                screens.peek().resume();
            } else {
                screens.removeValue(screen, true);
            }
            screen.hide();
            screen.dispose();
        }
        screensToRemove.clear();
    }

    @Override
    public void resize (int width, int height) {
        for (BaseScreen screen : screens) {
            screen.resize(width, height);
        }
    }

    public void removeScreen (@NotNull BaseScreen screen) {
        screensToRemove.add(screen);
    }

    public void addScreen (@NotNull BaseScreen screen) {
        screensToAdd.add(screen);
    }

    public void startNewGame() {
        engine.removeAllEntities();
        scriptManager.loadScript(Scene01_Load.class);
    }

    /**
     * Exit the game. Cleans up all the resources
     */
    public void exit() {
        //TODO: Actually clean up the resources
        Gdx.app.exit();
    }

    public class InputMultiplexer implements InputProcessor {
        public boolean keyDown(int keycode) {
            for (int i = screens.size-1; i >= 0; --i)
                if (screens.get(i).getInputProcessor().keyDown(keycode)) return true;
            return false;
        }

        public boolean keyUp(int keycode) {
            for (int i = screens.size-1; i >= 0; --i)
                if (screens.get(i).getInputProcessor().keyUp(keycode)) return true;
            return false;
        }

        public boolean keyTyped(char character) {
            for (int i = screens.size-1; i >= 0; --i)
                if (screens.get(i).getInputProcessor().keyTyped(character)) return true;
            return false;
        }

        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            for (int i = screens.size-1; i >= 0; --i)
                if (screens.get(i).getInputProcessor().touchDown(screenX, screenY, pointer, button)) return true;
            return false;
        }

        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            for (int i = screens.size-1; i >= 0; --i)
                if (screens.get(i).getInputProcessor().touchUp(screenX, screenY, pointer, button)) return true;
            return false;
        }

        public boolean touchDragged(int screenX, int screenY, int pointer) {
            for (int i = screens.size-1; i >= 0; --i)
                if (screens.get(i).getInputProcessor().touchDragged(screenX, screenY, pointer)) return true;
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            for (int i = screens.size-1; i >= 0; --i)
                if (screens.get(i).getInputProcessor().mouseMoved(screenX, screenY)) return true;
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            for (int i = screens.size-1; i >= 0; --i)
                if (screens.get(i).getInputProcessor().scrolled(amount)) return true;
            return false;
        }
    }
}
