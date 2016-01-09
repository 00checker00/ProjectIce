package de.project.ice.scripting.scripts

import com.badlogic.ashley.core.Entity
import de.project.ice.Storage
import de.project.ice.ecs.Components
import de.project.ice.ecs.components.InvisibilityComponent
import de.project.ice.scripting.Script

class TeapotFlame : Script() {
    var teaReady = false

    override fun onUpdateEntity(entity: Entity, delta: Float) {
        super.onUpdateEntity(entity, delta)
        if (Storage.SAVESTATE.getBoolean("scene_03_oven_fire")) {
            entity.remove(InvisibilityComponent::class.java)
            if (Components.transform.get(entity).scale.y < 1f) {
                Components.transform.get(entity).scale.scl(1f, 1f + 0.2f * delta)
            } else if (!teaReady) {
                teaReady = true
                Storage.SAVESTATE.put("scene_03_tea_ready", true)
            }
        }
    }
}
