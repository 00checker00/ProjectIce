package de.project.ice.ecs.components

import de.project.ice.annotations.Property


class DistanceScaleComponent : CopyableIceComponent {
    @Property("The position of the horizon")
    var targetY: Float = 20.0f
    @Property("The scale of the entity when it has reached the horizon")
    var targetScale: Float = 1.0f
    @Property("The current scale", true)
    var currentScale: Float = 1.0f

    override fun copyTo(copy: CopyableIceComponent) {
        if (copy is DistanceScaleComponent) {
            copy.targetScale = targetScale
            copy.currentScale = currentScale
            copy.targetY = targetY
        }
    }


    override fun reset() {
        targetScale = 1.0f
        currentScale = 1.0f
        targetY = 20.0f
    }

}