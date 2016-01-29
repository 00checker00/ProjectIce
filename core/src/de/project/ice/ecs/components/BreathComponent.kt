package de.project.ice.ecs.components

import de.project.ice.annotations.Property

/**
 * Component for idle breathing animation of characters
 */
class BreathComponent : CopyableIceComponent {
    @Property("The current scaling factor", true)
    val curScale = Vector2(0f, 0f)
    @Property("The maximal scale of a breath")
    val scaleValue = Vector2(0f, 0f)
    @Property("The duration of a breath")
    var duration = 1f
    @Property("The current animation time", true)
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


