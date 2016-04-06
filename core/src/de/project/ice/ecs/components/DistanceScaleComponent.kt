package de.project.ice.ecs.components

import de.project.ice.annotations.Property


class DistanceScaleComponent : CopyableIceComponent {
    @Property("The current scale", true)
    var currentScale: Float = 1.0f

    override fun copyTo(copy: CopyableIceComponent) {
        if (copy is DistanceScaleComponent) {
            copy.currentScale = currentScale
        }
    }


    override fun reset() {
        currentScale = 1.0f
    }

}