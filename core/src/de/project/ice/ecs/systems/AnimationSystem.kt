package de.project.ice.ecs.systems

import com.badlogic.ashley.core.Entity
import de.project.ice.ecs.Components
import de.project.ice.ecs.Families
import de.project.ice.utils.Assets

class AnimationSystem : IteratingIceSystem(Families.animated) {

    public override fun processEntity(entity: Entity, deltaTime: Float) {
        val tex = Components.texture.get(entity)
        val anim = Components.animation.get(entity)

        anim.time = anim.time + deltaTime

        var animationNumber = anim.animation
        if (Components.walking.has(entity)) {
            val walkingComponent = Components.walking.get(entity)
            if (walkingComponent.isWalking) {
                animationNumber = walkingComponent.animation
            }
        }

        val animation = anim.animations.get(animationNumber)
        if (animation != null && animation.data != null && anim.animation > 0) {
            try {
                tex.region = Assets.Holder.TextureRegion(animation.name, animation.data.getKeyFrame(anim.time))
            } catch (ignore: ArithmeticException) {
            }
            if (animation.data.isAnimationFinished(anim.time)) {
                anim.animation = 1
            }
        }
    }
}
