package de.project.ice;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import de.project.ice.screens.BaseScreen;
import de.project.ice.screens.GameScreen;
import org.jetbrains.annotations.NotNull;

public class IceGame extends ApplicationAdapter {
    public SpriteBatch batch;
    private final DelayedRemovalArray<BaseScreen> screens = new DelayedRemovalArray<BaseScreen>();

    @Override
    public void create () {
        batch = new SpriteBatch();
        GameScreen gameScreen = new GameScreen(this);
        addScreen(gameScreen);
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
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
}
