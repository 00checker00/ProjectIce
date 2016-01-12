package de.project.ice.ecs.systems

import com.badlogic.ashley.core.Entity
import de.project.ice.ecs.Components
import de.project.ice.ecs.Families
import de.project.ice.ecs.getComponent


class DistanceScaleSystem : IteratingIceSystem(Families.distanceScale) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity.getComponent(Components.transform)
        val distanceScale = entity.getComponent(Components.distanceScale)

        val y = Math.min(transform.pos.y, distanceScale.targetY) // Cap y
        val factor = y / distanceScale.targetY

        // linear interpolate the scale factor
        val newScale = 1.0f - factor + factor * distanceScale.targetScale;

        if (newScale != distanceScale.currentScale) {

            val scale = newScale / distanceScale.currentScale
            transform.scale.scl(scale);

            distanceScale.currentScale = newScale
        }
    }


}