package de.project.ice.ecs.components


import com.badlogic.gdx.math.Vector2
import de.project.ice.annotations.Property
import java.util.*

/**
 * Component for movable objects and characters
 */
class MoveComponent : CopyableIceComponent {
    @Property("Whether the entity is currently moving or not", true)
    var isMoving = false
    @Property("The position to where this entity is walking")
    val targetPositions = ArrayList<Vector2>()
    @Property("The moving speed of this entity")
    var speed = 2.0f
    var callback: (()->Unit)? = null;

    override fun reset() {
        isMoving = false
        speed = 2.0f
        targetPositions.clear()
        callback = null
    }

    override fun copyTo(copy: CopyableIceComponent) {
        if (copy is MoveComponent) {
            copy.isMoving = isMoving
            copy.speed = speed
            copy.callback = callback
            for (pos in targetPositions) {
                copy.targetPositions.add(pos.cpy())
            }
        }
    }
}
