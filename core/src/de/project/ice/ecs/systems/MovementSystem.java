package de.project.ice.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Vector2;
import de.project.ice.ecs.Components;
import de.project.ice.ecs.IceEngine;
import de.project.ice.ecs.components.MoveComponent;
import de.project.ice.ecs.components.TransformComponent;

public class MovementSystem extends IteratingIceSystem
{
    @SuppressWarnings("unchecked")
    public MovementSystem()
    {
        super(Family.all(MoveComponent.class).get());
    }

    @Override
    public void addedToEngine(IceEngine engine)
    {
        super.addedToEngine(engine);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime)
    {
        MoveComponent move = Components.move.get(entity);
        TransformComponent t = Components.transform.get(entity);

        if (move.targetPositions.size() > 0)
        {
            Vector2 targetVector = move.targetPositions.get(0); // getAnimation the next waypoint
            Vector2 directionVector = targetVector.cpy().sub(t.pos.x, t.pos.y).nor(); // calculate direction vector
            Vector2 velocityVector = directionVector.scl(move.speed); // calculate velocity with specified duration
            Vector2 movementVector = velocityVector.scl(deltaTime); // calculates the movement vector for the CURRENT UPDATE!

            // if the destination has not been reached
            if (t.pos.dst2(targetVector.x, targetVector.y) > movementVector.len2())
            {
                // Gdx.app.log("handleMovement", "" + movementVector.len());
                t.pos.add(movementVector.x, movementVector.y);
                move.isMoving = true;
                if (directionVector.x > 0)
                {
                    t.flipHorizontal = true;
                }
                else if (directionVector.x < 0)
                {
                    t.flipHorizontal = false;
                }

                // Start walk animation
                if (Components.walking.has(entity))
                {
                    Components.walking.get(entity).isWalking = true;
                }
            }
            else
            {
                t.pos.set(targetVector.x, targetVector.y);
                move.isMoving = false;
                move.targetPositions.remove(0); // remove path

                // Target reached
                if (move.targetPositions.isEmpty())
                {
                    entity.remove(MoveComponent.class);

                    // Stop walk animation
                    if (Components.walking.has(entity))
                    {
                        Components.walking.get(entity).isWalking = false;
                    }
                }
            }
        }
    }
}