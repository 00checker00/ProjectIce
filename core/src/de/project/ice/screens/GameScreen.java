package de.project.ice.screens;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import de.project.ice.IceGame;
import de.project.ice.ecs.IceEngine;
import de.project.ice.scripting.ScriptManager;
import de.project.ice.scripting.scripts.Scene01_Load;
import de.project.ice.utils.DelegatingBlockingInputProcessor;
import org.jetbrains.annotations.NotNull;

/**
 * Input Handling here...
 */
public class GameScreen extends BaseScreenAdapter {
    @NotNull
    private final IceEngine engine;
    @NotNull
    private final ScriptManager scriptManager;
    private final EntitySystem[] SystemsToPause;

    public GameScreen (@NotNull IceGame game) {
        super(game);
        this.engine = new IceEngine(game);
        this.scriptManager = new ScriptManager(this.engine);

        // Load "Scene01" by loading the Scene01_Load script
        this.scriptManager.loadScript(Scene01_Load.class);

        inputProcessor = new DelegatingBlockingInputProcessor(engine.controlSystem) {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    GameScreen.this.game.addScreen(new PauseScreen(GameScreen.this.game));
                    return true;
                }
                return super.keyDown(keycode);
            }
        };
        SystemsToPause = new EntitySystem[] {
                engine.stateSystem,
                engine.cameraSystem,
                engine.animationSystem,
                engine.controlSystem,
                engine.movementSystem,
                engine.scriptingSystem
        };
    }

    @Override
    public void update (float delta) {
        engine.update(delta);
    }

    @Override
    public void resume () {
        for (EntitySystem system : SystemsToPause)
            system.setProcessing(true);
    }

    @Override
    public void pause () {
        for (EntitySystem system : SystemsToPause)
            system.setProcessing(false);
    }

    @Override
    public int getPriority () {
        return 1000;
    }
}
