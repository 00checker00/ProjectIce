package de.project.ice;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import de.project.ice.screens.BaseScreen;
import de.project.ice.screens.GameScreen;
import de.project.ice.screens.MainMenuScreen;
import org.jetbrains.annotations.NotNull;

public class IceGame extends ApplicationAdapter {
    public static TextureAtlas textureAtlas;
    private final DelayedRemovalArray<BaseScreen> screens = new DelayedRemovalArray<BaseScreen>();
    public SpriteBatch batch;

    @Override
    public void create () {
        batch = new SpriteBatch();
        textureAtlas = new TextureAtlas(Gdx.files.internal("spritesheets/eksi2.atlas"));
        BaseScreen mainMenuScreen = new MainMenuScreen(this);
        addScreen(mainMenuScreen);

        Gdx.input.setInputProcessor(new InputMultiplexer());
    }

    @Override
    public void dispose () {
        screens.begin();
        for (BaseScreen screen : screens) {
            screen.hide();
        }
        screens.end();
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

    @Override
    public void render () {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        float delta = Gdx.graphics.getDeltaTime();
        screens.begin();
        for (BaseScreen screen : screens) {
            screen.update(delta);
        }
        screens.end();
        for (BaseScreen screen : screens) {
            screen.render();
        }
    }

    @Override
    public void resize (int width, int height) {
        screens.begin();
        for (BaseScreen screen : screens) {
            screen.resize(width, height);
        }
        screens.end();
    }

    public void removeScreen (@NotNull BaseScreen screen) {
        if (screen == screens.peek()) {
            screens.pop();
            screens.peek().resume();
        } else {
            screens.removeValue(screen, true);
        }
        screen.hide();
    }

    public void addScreen (@NotNull BaseScreen screen) {
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

    public void startNewGame() {
        addScreen(new GameScreen(this));
    }

    /**
     * Exit the game. Cleans up all the resources
     */
    public void exit() {
        //TODO: Actually clean up the resources
        Gdx.app.exit();
    }

    private class InputMultiplexer implements InputProcessor {
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
