package de.project.ice.ecs.components

import de.project.ice.annotations.Property

class PathPlanningComponent : CopyableIceComponent {
    @Property("The first waypoint")
    var start = Vector2(0f, 0f)
    @Property("The target position")
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
