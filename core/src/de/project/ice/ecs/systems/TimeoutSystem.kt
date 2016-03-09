package de.project.ice.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import de.project.ice.ecs.Components
import de.project.ice.ecs.components.TimeoutComponent
import de.project.ice.ecs.getComponent


class TimeoutSystem: IteratingIceSystem(Family.all(TimeoutComponent::class.java).get()) {
    override fun processEntity(entity: Entity, delta: Float) {
        val timeout = entity.getComponent(Components.timeout)
        timeout.timeout -= delta;
        if (timeout.timeout <= 0) {
            timeout.function?.invoke()
            engine.removeEntity(entity)
        }
    }
}