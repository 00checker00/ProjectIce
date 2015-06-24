package de.project.ice.editor;


import com.badlogic.gdx.Gdx;
import de.project.ice.IceGame;
import de.project.ice.Storage;

public class EditorApplication extends IceGame {
    @Override
    protected void init() {
        Gdx.input.setInputProcessor(new InputMultiplexer());
        pauseGame();
        addScreen(new EditorScreen(this));
        Gdx.input.setCursorImage(null, 0, 0);
    }
}
