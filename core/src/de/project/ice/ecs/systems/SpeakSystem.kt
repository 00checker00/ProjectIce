package de.project.ice.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.Gdx
import de.project.ice.ecs.Components
import de.project.ice.ecs.components.SpeakComponent
import de.project.ice.ecs.getComponent


class SpeakSystem: IteratingIceSystem(Family.all(SpeakComponent::class.java).get()) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val speakComponent = entity.getComponent(Components.speak)

        if (speakComponent.target == null) {
            speakComponent.target = engine.getEntityByName(speakComponent.targetName)

            if (speakComponent.target == null) {
                Gdx.app.log(javaClass.simpleName, "Couldn't find speaking entity: ${speakComponent.targetName}")
                entity.remove(SpeakComponent::class.java)
                return
            }
        }
    }

}