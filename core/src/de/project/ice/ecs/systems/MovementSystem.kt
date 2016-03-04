package de.project.ice.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import de.project.ice.ecs.Components
import de.project.ice.ecs.IceEngine
import de.project.ice.ecs.components.MoveComponent

class MovementSystem : IteratingIceSystem(Family.all(MoveComponent::class.java).get()) {

    override fun addedToEngine(engine: IceEngine) {
        super.addedToEngine(engine)
    }

    public override fun processEntity(entity: Entity, deltaTime: Float) {
        val move = Components.move.get(entity)
        val t = Components.transform.get(entity)

        if (move.targetPositions.size > 0) {
            val targetVector = move.targetPositions[0] // getAnimation the next waypoint
            val directionVector = targetVector.cpy().sub(t.pos.x, t.pos.y).nor() // calculate direction vector
            val velocityVector = directionVector.scl(move.speed) // calculate velocity with specified duration
            val movementVector = velocityVector.scl(deltaTime) // calculates the movement vector for the CURRENT UPDATE!

            // if the destination has not been reached
            if (t.pos.dst2(targetVector.x, targetVector.y) > movementVector.len2()) {
                // Gdx.app.log("handleMovement", "" + movementVector.len());
                t.pos.add(movementVector.x, movementVector.y)
                move.isMoving = true
                if (directionVector.x > 0) {
                    t.flipHorizontal = false
                } else if (directionVector.x < 0) {
                    t.flipHorizontal = true
                }

                // Start walk animation
                if (Components.walking.has(entity)) {
                    Components.walking.get(entity).isWalking = true
                }
            } else {
                t.pos.set(targetVector.x, targetVector.y)
                move.isMoving = false
                move.targetPositions.removeAt(0) // remove path

                // Target reached
                if (move.targetPositions.isEmpty()) {
                    entity.remove(MoveComponent::class.java)

                    // Stop walk animation
                    if (Components.walking.has(entity)) {
                        Components.walking.get(entity).isWalking = false
                    }
                }
            }
        }
    }
}