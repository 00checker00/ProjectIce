package de.project.ice.ecs.components

import de.project.ice.annotations.Property

/**
 * Component for controlling the main character via input (keyboard, mouse)
 */
class ControlComponent : IceComponentAdapter() {
    @Property("The moving speed of this entity")
    var speed = 2.0f

    override fun reset() {
        speed = 2.0f
    }

    override fun copyTo(copy: CopyableIceComponent) {
        if (copy is ControlComponent) {
            copy.speed = speed
        }
    }
}


