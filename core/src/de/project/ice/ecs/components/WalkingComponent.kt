package de.project.ice.ecs.components

import de.project.ice.annotations.Property

class WalkingComponent : IceComponent {
    @Property("Whether the entity is currently walking.", true)
    var isWalking = false
    @Property("The index of the walking animation for this entity")
    var animation = 0
    @Property("Whether the entity will wiggle while walking (SoutPark Style)", group = "wiggle")
    var wiggle = false
    @Property("The speed of the wiggling", group = "wiggle")
    var wiggleSpeed = 0.5f
    @Property("The height of the wiggling", group = "wiggle")
    var wiggleHeight = 0.03f
    @Property("The strength of the wiggling", group = "wiggle")
    var wiggleStrength = 2.5f
    @Property("The current alpha of the wiggle animation", true, "wiggle")
    var wiggleAlpha = 0.0f

    override fun reset() {
        isWalking = false
        animation = 0
        wiggle = false
        wiggleAlpha = 0.0f
        wiggleSpeed = 0.5f
        wiggleHeight = 0.03f
        wiggleStrength = 2.5f
    }

    fun copyTo(copy: IceComponent) {
        if (copy is WalkingComponent) {
            copy.isWalking = isWalking
            copy.animation = animation
            copy.wiggle = wiggle
            copy.wiggleAlpha = wiggleAlpha
            copy.wiggleSpeed = wiggleSpeed
            copy.wiggleHeight = wiggleHeight
            copy.wiggleStrength = wiggleStrength
        }
    }
}
