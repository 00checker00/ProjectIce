package de.project.ice.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import de.project.ice.ecs.Components;
import de.project.ice.ecs.components.CameraComponent;
import de.project.ice.ecs.components.TransformComponent;

/**
 * all camera settings here
 * creation, settings
 */
public class CameraSystem extends IteratingSystem {
    /**
     * x-axis dimension from far left to far right
     */
    static final float FRUSTUM_WIDTH = 16f;
    /**
     * y-axis dimension from bottom to top
     */
    static final float FRUSTUM_HEIGHT = 9f;
    static final float PIXELS_TO_METRES = 1.0f / 128.0f;

    @SuppressWarnings("unchecked")
    public CameraSystem () {
        super(Family.all(CameraComponent.class).get());
    }

    @Override
    public void processEntity (Entity entity, float deltaTime) {
        CameraComponent cam = Components.camera.get(entity);

        if (cam.camera == null) {
            cam.camera = new OrthographicCamera(FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
            cam.camera.position.set(FRUSTUM_WIDTH / 2, FRUSTUM_HEIGHT / 2, 0);
        }

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }
}
