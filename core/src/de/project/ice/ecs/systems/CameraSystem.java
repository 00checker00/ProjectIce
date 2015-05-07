package de.project.ice.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import de.project.ice.ecs.Components;
import de.project.ice.ecs.components.CameraComponent;
import de.project.ice.ecs.components.TransformComponent;

public class CameraSystem extends IteratingSystem {

    @SuppressWarnings("unchecked")
    public CameraSystem () {
        super(Family.all(CameraComponent.class).get());
    }

    @Override
    public void processEntity (Entity entity, float deltaTime) {
        CameraComponent cam = Components.camera.get(entity);

        if (cam.target == null || cam.camera == null) {
            return;
        }

        TransformComponent target = Components.transform.get(cam.target);

        if (target == null) {
            return;
        }

        cam.camera.position.y = Math.max(cam.camera.position.y, target.pos.y);
    }
}
