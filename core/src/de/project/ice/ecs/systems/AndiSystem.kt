package de.project.ice.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.graphics.g2d.Animation
import de.project.ice.ecs.Components
import de.project.ice.ecs.components.AndiComponent
import de.project.ice.ecs.hasComponent
import de.project.ice.utils.Assets
import de.project.ice.utils.editComponents


class AndiSystem: IteratingIceSystem(Family.all(AndiComponent::class.java).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity.editComponents(engine) {
            if (!entity.hasComponent(Components.animation)) {
                if (!entity.hasComponent(Components.animation))
                    AnimationComponent {
                        animations.put(1, Assets.createAnimation("andi_idle", Float.POSITIVE_INFINITY, Animation.PlayMode.REVERSED))
                        animations.put(2, Assets.createAnimation("andi_lauf", 0.03f, Animation.PlayMode.LOOP))
                        animations.put(3, Assets.createAnimation("andi_buecken", 0.03f, Animation.PlayMode.NORMAL))
                        animation = 1
                    }
            }

            if (!entity.hasComponent(Components.transform)) {
               TransformComponent {  }
            }

            if (!entity.hasComponent(Components.texture)) {
                TextureComponent {
                    region = Assets.findRegion("andi_idle")
                }
            }

            if (!entity.hasComponent(Components.control)) {
                ControlComponent {
                    speed = 2f
                }
            }

            if (!entity.hasComponent(Components.walking)) {
                WalkingComponent {
                    animation = 0
                    wiggle = true
                }
            }

            if (!entity.hasComponent(Components.distanceScale)) {
                DistanceScaleComponent {  }
            }


            if (!entity.hasComponent(Components.hotspot)) {
                HotspotComponent {
                    height = 1.3f
                }
            }
        }
    }
}