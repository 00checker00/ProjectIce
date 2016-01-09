package de.project.ice.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import de.project.ice.ecs.Components
import de.project.ice.ecs.components.MoveComponent
import de.project.ice.ecs.components.UseComponent
import de.project.ice.hotspot.Hotspots

class UseSystem : IteratingIceSystem(Family.all(UseComponent::class.java).exclude(MoveComponent::class.java).get()) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val use = Components.use.get(entity)

        if (use.target == null || !Components.hotspot.has(use.target)) {
            throw RuntimeException("Target is invalid")
        }
        val hotspotComponent = Components.hotspot.get(use.target)


        val hotspot = Hotspots[hotspotComponent.script]

        if (hotspot != null) {
            if (use.withItem != null) {
                hotspot.useWith(engine.game, use.withItem!!.name)
            } else {
                hotspot.use(engine.game, use.cursor)
            }
        }
        entity.remove(UseComponent::class.java)
    }
}
