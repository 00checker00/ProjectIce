package de.project.ice.ecs

import com.badlogic.ashley.core.ComponentMapper
import de.project.ice.ecs.components.*

object Components {
    val animation = ComponentMapper.getFor(AnimationComponent::class.java)
    @JvmField val camera = ComponentMapper.getFor(CameraComponent::class.java)
    @JvmField val texture = ComponentMapper.getFor(TextureComponent::class.java)
    @JvmField val transform = ComponentMapper.getFor(TransformComponent::class.java)
    val disabled = ComponentMapper.getFor(DisabledComponent::class.java)
    val script = ComponentMapper.getFor(ScriptComponent::class.java)
    val control = ComponentMapper.getFor(ControlComponent::class.java)
    val move = ComponentMapper.getFor(MoveComponent::class.java)
    @JvmField val breath = ComponentMapper.getFor(BreathComponent::class.java)
    val name = ComponentMapper.getFor(NameComponent::class.java)
    @JvmField val hotspot = ComponentMapper.getFor(HotspotComponent::class.java)
    @JvmField val walkarea = ComponentMapper.getFor(WalkAreaComponent::class.java)
    val use = ComponentMapper.getFor(UseComponent::class.java)
    @JvmField val pathplanning = ComponentMapper.getFor(PathPlanningComponent::class.java)
    val walking = ComponentMapper.getFor(WalkingComponent::class.java)

}
