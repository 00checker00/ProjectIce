package de.project.ice.ecs.components

import com.badlogic.gdx.math.Vector2

/**
 * Component for idle breathing animation of characters
 */
class BreathComponent : CopyableIceComponent {
    val curScale = Vector2(0f, 0f)
    val scaleValue = Vector2(0f, 0f)
    var duration = 1f
    var time = 0.0f

    override fun reset() {
        curScale.set(0f, 0f)
        scaleValue.set(0f, 0f)
        duration = 1f
        time = 0.0f
    }

    override fun copyTo(copy: CopyableIceComponent) {
        if (copy is BreathComponent) {
            copy.curScale.set(curScale)
            copy.scaleValue.set(scaleValue)
            copy.duration = duration
            copy.time = time
        }
    }
}


