package de.project.ice.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import de.project.ice.IceGame;
import de.project.ice.ecs.Engine;
import de.project.ice.scripting.ScriptManager;
import de.project.ice.scripting.scripts.Scene01_Load;
import de.project.ice.utils.DelegatingBlockingInputProcessor;
import org.jetbrains.annotations.NotNull;

/**
 * Input Handling here...
 */
public class GameScreen extends BaseScreenAdapter {
    @NotNull
    private final Engine engine;
    @NotNull
    private final ScriptManager scriptManager;

    public GameScreen (@NotNull IceGame game) {
        super(game);
        this.engine = new Engine(game);
        this.scriptManager = new ScriptManager(this.engine);

        // Load "Scene01" by loading the Scene01_Load script
        this.scriptManager.loadScript(Scene01_Load.class);
    }

    @Override
    public void update (float delta) {
        engine.update(delta);
    }

    @Override
    public void resume () {
        engine.stateSystem.setProcessing(true);
        engine.animationSystem.setProcessing(true);
        engine.cameraSystem.setProcessing(true);
    }

    @Override
    public void pause () {
        engine.stateSystem.setProcessing(false);
        engine.animationSystem.setProcessing(false);
        engine.cameraSystem.setProcessing(false);
    }

    @Override
    public int getPriority () {
        return 1000;
    }

    @NotNull
    @Override
    public InputProcessor getInputProcessor() {
        return new DelegatingBlockingInputProcessor(engine.controlSystem) {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    game.addScreen(new PauseScreen(game));
                    return true;
                }
                return super.keyDown(keycode);
            }
        };
    }
}
