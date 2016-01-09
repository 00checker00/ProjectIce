package de.project.ice.ecs.systems

import com.badlogic.ashley.core.Entity
import de.project.ice.ecs.Components
import de.project.ice.ecs.Families

class BreathSystem : IteratingIceSystem(Families.breathing) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val breath = Components.breath.get(entity)
        breath.time = breath.time + deltaTime
        breath.curScale.set(breath.scaleValue.cpy().scl(Math.sin((breath.time / breath.duration).toDouble()).toFloat()))
    }
}
