package de.project.ice.ecs

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import de.project.ice.ecs.components.*

object Components {
    val animation = ComponentMapper.getFor(AnimationComponent::class.java)
    val camera = ComponentMapper.getFor(CameraComponent::class.java)
    val texture = ComponentMapper.getFor(TextureComponent::class.java)
    @JvmField val transform = ComponentMapper.getFor(TransformComponent::class.java)
    val disabled = ComponentMapper.getFor(DisabledComponent::class.java)
    val script = ComponentMapper.getFor(ScriptComponent::class.java)
    val control = ComponentMapper.getFor(ControlComponent::class.java)
    val move = ComponentMapper.getFor(MoveComponent::class.java)
    val breath = ComponentMapper.getFor(BreathComponent::class.java)
    val name = ComponentMapper.getFor(NameComponent::class.java)
    val hotspot = ComponentMapper.getFor(HotspotComponent::class.java)
    val walkarea = ComponentMapper.getFor(WalkAreaComponent::class.java)
    val use = ComponentMapper.getFor(UseComponent::class.java)
    val pathplanning = ComponentMapper.getFor(PathPlanningComponent::class.java)
    val walking = ComponentMapper.getFor(WalkingComponent::class.java)
    val distanceScale = ComponentMapper.getFor(DistanceScaleComponent::class.java)
}

fun Entity.hasComponent(mapper: ComponentMapper<IceComponent>): Boolean = mapper.has(this)
fun <T : IceComponent> Entity.getComponent(mapper: ComponentMapper<T>): T = mapper.get(this)