package de.project.ice.ecs;

import com.badlogic.ashley.core.PooledEngine;
import de.project.ice.IceGame;
import de.project.ice.ecs.systems.*;
import org.jetbrains.annotations.NotNull;

public class IceEngine extends PooledEngine {
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
    @NotNull
    public final MovementSystem movementSystem;
    @NotNull
    public final ControlSystem controlSystem;

    public IceEngine() {
        super();
        stateSystem = new StateSystem();
        animationSystem = new AnimationSystem();
        cameraSystem = new CameraSystem();
        renderingSystem = new RenderingSystem();
        scriptingSystem = new ScriptingSystem();
        movementSystem = new MovementSystem();
        controlSystem = new ControlSystem();

        addSystem(stateSystem);
        addSystem(animationSystem);
        addSystem(cameraSystem);
        addSystem(renderingSystem);
        addSystem(scriptingSystem);
        addSystem(movementSystem);
        addSystem(controlSystem);
    }
}
