package de.project.ice.ecs;

import com.badlogic.ashley.core.PooledEngine;
import de.project.ice.IceGame;
import de.project.ice.ecs.systems.*;
import org.jetbrains.annotations.NotNull;

public class Engine extends PooledEngine {
    @NotNull
    public final StateSystem stateSystem;
    @NotNull
    public final AnimationSystem animationSystem;
    @NotNull
    public final RenderingSystem renderingSystem;
    @NotNull
    public final CameraSystem cameraSystem;
    @NotNull
    public final ScriptingSystem scriptingSystem;

    public Engine (IceGame iceGame) {
        super();
        stateSystem = new StateSystem();
        animationSystem = new AnimationSystem();
        cameraSystem = new CameraSystem();
        renderingSystem = new RenderingSystem(iceGame.batch);
        scriptingSystem = new ScriptingSystem();

        addSystem(stateSystem);
        addSystem(animationSystem);
        addSystem(cameraSystem);
        addSystem(renderingSystem);
        addSystem(scriptingSystem);
    }
}
