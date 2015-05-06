package de.project.ice.ecs;

import com.badlogic.ashley.core.PooledEngine;
import de.project.ice.IceGame;
import de.project.ice.ecs.systems.AnimationSystem;
import de.project.ice.ecs.systems.CameraSystem;
import de.project.ice.ecs.systems.RenderingSystem;
import de.project.ice.ecs.systems.StateSystem;
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

    public Engine (IceGame iceGame) {
        super();
        stateSystem = new StateSystem();
        animationSystem = new AnimationSystem();
        cameraSystem = new CameraSystem();
        renderingSystem = new RenderingSystem(iceGame.batch);
    }
}
