package de.project.ice.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Vector2;
import de.project.ice.IceGame;
import de.project.ice.ecs.Components;
import de.project.ice.ecs.IceEngine;
import de.project.ice.ecs.components.HotspotComponent;
import de.project.ice.ecs.components.MovableComponent;
import de.project.ice.ecs.components.TransformComponent;
import de.project.ice.ecs.components.UseComponent;
import de.project.ice.hotspot.HotspotManager;

public class MovementSystem extends IteratingIceSystem {
    private IceGame game;

    @SuppressWarnings("unchecked")
    public MovementSystem() {
        super(Family.all( MovableComponent.class).get());
    }

    @Override
    public void addedToEngine(IceEngine engine) {
        super.addedToEngine(engine);
        game = engine.game;
    }

    @Override
    public void processEntity (Entity entity, float deltaTime) {
        MovableComponent move = Components.movable.get(entity);
        TransformComponent t = Components.transform.get(entity);

        if(move.targetPositions.size() > 0) {
            Vector2 targetVector = move.targetPositions.get(0); // getAnimation the next waypoint
            Vector2 directionVector = targetVector.cpy().sub(t.pos.x, t.pos.y).nor(); // calculate direction vector
            Vector2 velocityVector = directionVector.scl(move.speed); // calculate velocity with specified duration
            Vector2 movementVector = velocityVector.scl(deltaTime); // calculates the movement vector for the CURRENT UPDATE!

            // if the destination has not been reached
            if(t.pos.dst2(targetVector.x, targetVector.y) > movementVector.len2()) {
                // Gdx.app.log("handleMovement", "" + movementVector.len());
                t.pos.add(movementVector.x, movementVector.y);
                move.isMoving = true;
            } else {
                t.pos.set(targetVector.x, targetVector.y);
                move.isMoving = false;
                move.targetPositions.remove(0); // remove path

                // Target reached
                if (move.targetPositions.isEmpty()) {
                    onTargetReached(entity);
                }
            }
        }
    }

    private void onTargetReached(Entity entity) {
        if (Components.use.has(entity))  {
            UseComponent component = Components.use.get(entity);
            if (component.target == null ||
                    !Components.transform.has(component.target) ||
                    !Components.hotspot.has(component.target)) {
                throw new RuntimeException("Target is invalid");
            }
            HotspotComponent hotspotComponent = Components.hotspot.get(component.target);

            HotspotManager.Hotspot hotspot = game.hotspotManager.get(hotspotComponent.script);

            if (hotspot != null) {
                if (component.item != null)
                    hotspot.useWith(component.item);
                else
                    hotspot.use(component.cursor);
            }
        }
        entity.remove(UseComponent.class);
    }
}