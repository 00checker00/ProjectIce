package de.project.ice.ecs.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import de.project.ice.ecs.Components;
import de.project.ice.ecs.Families;
import de.project.ice.ecs.IceEngine;
import de.project.ice.ecs.components.*;
import de.project.ice.utils.FakePerspectiveCamera;
import javafx.scene.Camera;
import org.jetbrains.annotations.NotNull;

public class MovementSystem extends IteratingIceSystem {
    @NotNull
    @SuppressWarnings("NullableProblems")
    static final float MOVEMENT_SPEED = 2f;
    private ImmutableArray<Entity> cameras;
    FakePerspectiveCamera active_camera = null;

    @SuppressWarnings("unchecked")
    public MovementSystem() {
        super(Family.all(ControlComponent.class, MovableComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        // update the active camera
        if(cameras.size() > 0)
            active_camera = cameras.first().getComponent(CameraComponent.class).camera;

        if (active_camera == null)
            return;
        super.update(deltaTime);
    }

    @Override
    public void processEntity (Entity entity, float deltaTime) {
        MovableComponent move = Components.movable.get(entity);
        TransformComponent t = Components.transform.get(entity);

        if(move.targetPositions.size() > 0) {
            Vector2 targetVector = move.targetPositions.get(0); // getAnimation the next waypoint
            Vector2 directionVector = targetVector.cpy().sub(t.pos.x, t.pos.y).nor(); // calculate direction vector
            Vector2 velocityVector = directionVector.scl(MOVEMENT_SPEED); // calculate velocity with specified speed
            Vector2 movementVector = velocityVector.scl(deltaTime); // calculates the movement vector for the CURRENT UPDATE!

            float scaling = active_camera.calcDistanceScaling(t.pos.y);
            movementVector.scl(scaling);

            // if the destination has not been reached
            if(t.pos.dst2(targetVector.x, targetVector.y) > movementVector.len2()) {
                // Gdx.app.log("handleMovement", "" + movementVector.len());
                t.pos.add(movementVector.x, movementVector.y);
                move.isMoving = true;
            } else {
                //Gdx.app.log("handleMovement", "not reached!");
                t.pos.set(targetVector.x, targetVector.y);
                move.isMoving = false;
                move.targetPositions.remove(0); // remove path
            }
        }
    }

    @Override
    public void addedToEngine(IceEngine engine) {
        super.addedToEngine(engine);
        cameras = engine.getEntitiesFor(Families.camera);
    }
}