package de.project.ice.ecs

import com.badlogic.ashley.core.Family
import de.project.ice.ecs.components.*

@SuppressWarnings("unchecked")
object Families {
    @JvmField val renderable = Family.all(TransformComponent::class.java).exclude(InvisibilityComponent::class.java).get()
    @JvmField val animated = Family.all(TransformComponent::class.java, TextureComponent::class.java, AnimationComponent::class.java).exclude(InvisibilityComponent::class.java).get()
    @JvmField val scripted = Family.all(ScriptComponent::class.java).exclude(DisabledComponent::class.java).get()
    @JvmField val playerCharacter = Family.all(TransformComponent::class.java, TextureComponent::class.java, AnimationComponent::class.java, BreathComponent::class.java, ControlComponent::class.java).exclude(InvisibilityComponent::class.java).get()
    @JvmField val camera = Family.all(CameraComponent::class.java).exclude(DisabledComponent::class.java).get()
    @JvmField val breathing = Family.all(BreathComponent::class.java, IdleComponent::class.java).exclude(InvisibilityComponent::class.java, DisabledComponent::class.java).get()
    @JvmField val hotspot = Family.all(HotspotComponent::class.java, TransformComponent::class.java).exclude(DisabledComponent::class.java).get()
    @JvmField val walkArea = Family.all(WalkAreaComponent::class.java).exclude(DisabledComponent::class.java).get()
    @JvmField val controllable = Family.all(ControlComponent::class.java, TextureComponent::class.java, TransformComponent::class.java).exclude(DisabledComponent::class.java).get()
    @JvmField val distanceScale = Family.all(DistanceScaleComponent::class.java, TransformComponent::class.java).get()
    @JvmField val assets = Family.one(TextureComponent::class.java, AnimationComponent::class.java).get()
}
