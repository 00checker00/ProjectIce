package de.project.ice.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import de.project.ice.ecs.Components
import de.project.ice.ecs.components.BlendComponent
import de.project.ice.ecs.getComponent


class BlendSystem: IteratingIceSystem(Family.all(BlendComponent::class.java).get()) {
    override fun processEntity(entity: Entity, delta: Float) {
        val cameraComponent = entity.getComponent(Components.camera)
        val blendComponent = entity.getComponent(Components.blend)

        if (cameraComponent != null && blendComponent != null) {
            blendComponent.current += delta
            val alpha = blendComponent.interpolator.apply(blendComponent.current / blendComponent.duration)
            val color = Color(blendComponent.start).lerp(blendComponent.target, alpha)
            cameraComponent.color = color
        }
    }
}