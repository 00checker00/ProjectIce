package de.project.ice.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import de.project.ice.ecs.IceEngine
import de.project.ice.ecs.components.CameraComponent

/**
 * all camera settings here
 * creation, settings
 */
class CameraSystem() : IteratingIceSystem(Family.all(CameraComponent::class.java).get()) {
    private var renderingSystem: RenderingSystem? = null

    override fun addedToEngine(engine: IceEngine) {
        super.addedToEngine(engine)
        renderingSystem = engine.renderingSystem
    }

    public override fun processEntity(entity: Entity, deltaTime: Float) {
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
    }
}
