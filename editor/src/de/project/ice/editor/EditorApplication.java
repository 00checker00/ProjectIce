package de.project.ice.editor;


import com.badlogic.gdx.Gdx;
import de.project.ice.IceGame;
import de.project.ice.ecs.IceEngine;
import de.project.ice.screens.GameScreen;
import de.project.ice.scripting.ScriptManager;

public class EditorApplication extends IceGame {
    @Override
    public void create() {
        engine = new IceEngine();
        scriptManager = new ScriptManager(engine);
        gameScreen = new GameScreen(this, engine);
        addScreen(gameScreen);

        Gdx.input.setInputProcessor(new InputMultiplexer());
        pauseGame();
        addScreen(new EditorScreen(this));
    }
}
