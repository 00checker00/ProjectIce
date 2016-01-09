package de.project.ice.ecs.components

class WalkingComponent : IceComponent {
    var isWalking = false
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
