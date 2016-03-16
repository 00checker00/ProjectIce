package de.project.ice.ecs.components

import com.badlogic.gdx.math.Vector2
import de.project.ice.annotations.Property

class PathPlanningComponent : CopyableIceComponent {
    @Property("The first waypoint")
    var start = Vector2(0f, 0f)
    @Property("The target position")
    var target = Vector2(0f, 0f)
    @Property("The moving speed of this entity")
    var speed = 2.0f
    var callback: (()->Unit)? = null

    override fun reset() {
        start = Vector2()
        target = Vector2()
        speed = 2.0f
        callback = null;
    }

    override fun copyTo(copy: CopyableIceComponent) {
        if (copy is PathPlanningComponent) {
            copy.start = start.cpy()
            copy.target = target.cpy()
            copy.speed = speed
            copy.callback = callback
        }
    }
}
