package de.project.ice.ecs.components

import com.badlogic.gdx.math.Vector2

class HotspotComponent : CopyableIceComponent {
    val origin = Vector2(0f, 0f)
    val targetPos = Vector2(0f, 0f)
    var width = 0f
    var height = 0f
    var script = ""

    override fun reset() {
        origin.set(0f, 0f)
        targetPos.set(0f, 0f)
        width = 0f
        height = 0f
        script = ""
    }

    override fun copyTo(copy: CopyableIceComponent) {
        if (copy is HotspotComponent) {
            copy.origin.set(origin)
            copy.targetPos.set(targetPos)
            copy.width = width
            copy.height = height
            copy.script = script
        }
    }
}


