package de.project.ice.ecs.systems

import com.badlogic.ashley.core.Entity
import de.project.ice.ecs.Components
import de.project.ice.ecs.Families
import de.project.ice.ecs.getComponents
import de.project.ice.ecs.hasComponent
import de.project.ice.utils.Assets


class ReloadAssetsSystem: IteratingIceSystem(Families.assets) {
    override fun processEntity(entity: Entity, delta: Float) {
        if (entity.hasComponent(Components.animation)) {
            val animComponent = entity.getComponents(Components.animation)
            for (anim in animComponent.animations) {
                if (anim.value.isInvalidated) {
                    animComponent.animations.put(anim.key, Assets.reload(anim.value))
                }
            }
        }

        if (entity.hasComponent(Components.texture)) {
            val texComponent = entity.getComponents(Components.texture)
            if (texComponent.region.isInvalidated) {
                texComponent.region = Assets.reload(texComponent.region)
            }
        }
    }
}