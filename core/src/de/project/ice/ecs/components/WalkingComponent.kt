package de.project.ice.ecs.components

import de.project.ice.annotations.Property

class WalkingComponent : IceComponent {
    @Property("Whether the entity is currently walking.", true)
    var isWalking = false
    @Property("The index of the walking animation for this entity")
    var animation = 0

    override fun reset() {
        isWalking = false
        animation = 0
    }

    fun copyTo(copy: IceComponent) {
        if (copy is WalkingComponent) {
            copy.isWalking = isWalking
            copy.animation = animation
        }
    }
}
