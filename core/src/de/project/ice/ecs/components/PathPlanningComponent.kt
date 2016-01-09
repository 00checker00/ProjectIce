package de.project.ice.ecs.components

import com.badlogic.gdx.math.Vector2

class PathPlanningComponent : CopyableIceComponent {
    var start = Vector2(0f, 0f)
    var target = Vector2(0f, 0f)

    override fun reset() {
        start = Vector2()
        target = Vector2()
    }

    override fun copyTo(copy: CopyableIceComponent) {
        if (copy is PathPlanningComponent) {
            copy.start = start.cpy()
            copy.target = target.cpy()
        }
    }
}
