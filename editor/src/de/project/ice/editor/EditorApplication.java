package de.project.ice.editor;


import com.badlogic.gdx.Gdx;
import de.project.ice.IceGame;

public class EditorApplication extends IceGame
{
    @Override
    protected void init()
    {
        Gdx.input.setInputProcessor(new InputMultiplexer());
        pauseGame();
        addScreen(new EditGameScreen(this));
        addScreen(new EditorScreen(this));
        Gdx.graphics.newCursor(null, 0, 0).setSystemCursor();
    }
}
