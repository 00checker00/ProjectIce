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
import de.project.ice.ecs.components.*;
import javafx.scene.Camera;
import org.jetbrains.annotations.NotNull;

public class MovementSystem extends IteratingSystem {

    private ImmutableArray<Entity> cameras;

    static final float MOVEMENT_SPEED = 0.05f;

    @NotNull
    private Array<Entity> movementQueue = new Array<Entity>();

    @SuppressWarnings("unchecked")
    public MovementSystem() {
        super(Family.all(ControlComponent.class, MovableComponent.class).get());
    }

    @Override
    public void processEntity (Entity entity, float deltaTime) {
        movementQueue.add(entity);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // create the camera
        OrthographicCamera cam;
        if(cameras.size() > 0)
        {
            CameraComponent cameraComponent = cameras.first().getComponent(CameraComponent.class);
            cam = cameraComponent.camera;
        }
        else
            return;

        for(Entity entity : movementQueue)
        {
            MovableComponent move = Components.movable.get(entity);
            TransformComponent t = Components.transform.get(entity);


            // TODO check screen boundaries against sprite dimensions + non-walkable path
                if(move.isMoving) {
                    move.directionVector.set(move.targetUnprojected).sub(t.pos.x, t.pos.y).nor(); // calculate direction vector
                    move.velocityVector.set(move.directionVector).scl(MOVEMENT_SPEED); // calculate velocity with specified speed
                    move.movementVector.set(move.velocityVector).scl(deltaTime); // calculates the movement vector for the CURRENT UPDATE!

                    // if the destination has not been reached
                    if(t.pos.dst2(move.targetUnprojected.x, move.targetUnprojected.y, 0f) > move.movementVector.len2()) {
                       // Gdx.app.log("handleMovement", "" + movementVector.len());
                        t.pos.add(move.movementVector.x, move.movementVector.y, 0f);
                    }
                    else {
                        //Gdx.app.log("handleMovement", "not reached!");
                        t.pos.set(move.targetUnprojected.x, move.targetUnprojected.y, 0f);
                        move.isMoving = false;
                    }

                }
                else {
                    // is there a walkpath form a click?
                    if(move.targetPositions.size() > 0) {
                        if (cam != null) {
                            cam.unproject(move.targetPositions.get(0)); // unprojects UI coordinates to camera coordinates
                        }
                        move.targetUnprojected.set(move.targetPositions.get(0).x, move.targetPositions.get(0).y); // set the unprojected coordinates to new vector
                        move.targetPositions.remove(0); // remove path

                        // block moving over horizon
                        move.isMoving = move.targetUnprojected.y < (CameraSystem.FRUSTUM_HEIGHT * RenderingSystem.getScaleHorizonPercentage());
                    }
                }
        }

    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        cameras = engine.getEntitiesFor(Families.camera);
    }
}