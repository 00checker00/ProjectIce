package de.project.ice.ecs.components


class DistanceScaleComponent : CopyableIceComponent {
    var targetY: Float = 20.0f
    var targetScale: Float = 1.0f
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