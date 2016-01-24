package de.project.ice.scripting.scripts


import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.Animation
import de.project.ice.ecs.Components
import de.project.ice.ecs.components.*
import de.project.ice.scripting.Script
import de.project.ice.utils.Assets

class Teapot : Script() {

    override fun onAttachedToEntity(entity: Entity) {
        val flame = Engine().createEntity()

        val comp: Component? = null;


        val transformComponent = Engine().createComponent(TransformComponent::class.java)
        transformComponent.scale.set(1f, 0.1f)
        transformComponent.pos.set(Components.transform.get(entity).pos).add(0.01f, 0.45f)
        flame.add(transformComponent)

        val textureComponent = Engine().createComponent(TextureComponent::class.java)
        flame.add(textureComponent)

        val animationComponent = Engine().createComponent(AnimationComponent::class.java)
        animationComponent.animations.put(1, Assets.createAnimation("candle_fire", 0.13f, Animation.PlayMode.LOOP))
        animationComponent.animation = 1
        flame.add(animationComponent)

        val scriptComponent = Engine().createComponent(ScriptComponent::class.java)
        scriptComponent.scriptName = "TeapotFlame"
        flame.add(scriptComponent)

        val nameComponent = Engine().createComponent(NameComponent::class.java)
        nameComponent.name = "teapot_flame"
        flame.add(nameComponent)

        flame.add(InvisibilityComponent())
        Engine().addEntity(flame)
    }

    override fun onAttachedEntityRemoved(entity: Entity) {
        val flame = Engine().getEntityByName("teapot_flame")
        if (flame != null) {
            Engine().removeEntity(flame)
        }
    }
}
