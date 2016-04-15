package de.project.ice.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import de.project.ice.ecs.Components
import de.project.ice.ecs.Families
import de.project.ice.ecs.getComponents


class DistanceScaleSystem : IteratingIceSystem(Families.distanceScale) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val camera = engine.renderingSystem.activeCamera

        if (camera != null) {
            val transform = entity.getComponents(Components.transform)
            val distanceScale = entity.getComponents(Components.distanceScale)

            val y = Math.min(transform.pos.y, camera.targetY) // Cap y
            val alpha = y / camera.targetY

            // linear interpolate the scale alpha
            val newScale = 1.0f - alpha + alpha * camera.targetScale;

            distanceScale.currentScale = newScale
        }
    }


}