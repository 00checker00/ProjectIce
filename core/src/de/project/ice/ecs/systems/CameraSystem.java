package de.project.ice.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import de.project.ice.ecs.Components;
import de.project.ice.ecs.IceEngine;
import de.project.ice.ecs.components.CameraComponent;

/**
 * all camera settings here
 * creation, settings
 */
public class CameraSystem extends IteratingIceSystem {
    private RenderingSystem renderingSystem;

    @SuppressWarnings("unchecked")
    public CameraSystem () {
        super(Family.all(CameraComponent.class).get());
    }

    @Override
    public void addedToEngine(IceEngine engine) {
        super.addedToEngine(engine);
        renderingSystem = engine.renderingSystem;
    }

    @Override
    public void processEntity (Entity entity, float deltaTime) {
        CameraComponent cam = Components.camera.get(entity);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }
}
