package de.project.ice.screens;

import de.project.ice.IceGame;
import de.project.ice.ecs.Engine;
import org.jetbrains.annotations.NotNull;

public class GameScreen extends BaseScreenAdapter {
    @NotNull
    private final IceGame game;
    @NotNull
    private final Engine engine;

    public GameScreen (@NotNull IceGame game) {
        this.game = game;
        this.engine = new Engine(game);
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
}
