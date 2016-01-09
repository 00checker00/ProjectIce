package de.project.ice.ecs.components

import com.badlogic.gdx.math.Vector2
import java.util.*

/**
 * Component for movable objects and characters
 */
class MoveComponent : CopyableIceComponent {
    var isMoving = false
    val targetPositions = ArrayList<Vector2>()
    var speed = 2.0f

    override fun reset() {
        isMoving = false
        speed = 2.0f
        targetPositions.clear()
    }

    override fun copyTo(copy: CopyableIceComponent) {
        if (copy is MoveComponent) {
            copy.isMoving = isMoving
            copy.speed = speed
            for (pos in targetPositions) {
                copy.targetPositions.add(pos.cpy())
            }
        }
    }
}
