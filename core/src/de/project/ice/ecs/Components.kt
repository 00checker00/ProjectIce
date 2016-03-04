package de.project.ice.ecs

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import de.project.ice.ecs.components.*

object Components {
    public val animation = ComponentMapper.getFor(AnimationComponent::class.java)
    public val camera = ComponentMapper.getFor(CameraComponent::class.java)
    public val texture = ComponentMapper.getFor(TextureComponent::class.java)
    public val transform = ComponentMapper.getFor(TransformComponent::class.java)
    public val disabled = ComponentMapper.getFor(DisabledComponent::class.java)
    public val script = ComponentMapper.getFor(ScriptComponent::class.java)
    public val control = ComponentMapper.getFor(ControlComponent::class.java)
    public val move = ComponentMapper.getFor(MoveComponent::class.java)
    public val breath = ComponentMapper.getFor(BreathComponent::class.java)
    public val name = ComponentMapper.getFor(NameComponent::class.java)
    public val hotspot = ComponentMapper.getFor(HotspotComponent::class.java)
    public val walkarea = ComponentMapper.getFor(WalkAreaComponent::class.java)
    public val use = ComponentMapper.getFor(UseComponent::class.java)
    public val pathplanning = ComponentMapper.getFor(PathPlanningComponent::class.java)
    public val walking = ComponentMapper.getFor(WalkingComponent::class.java)
    public val distanceScale = ComponentMapper.getFor(DistanceScaleComponent::class.java)
}

fun Entity.hasComponent(mapper: ComponentMapper<IceComponent>): Boolean = mapper.has(this)
fun <T : IceComponent> Entity.getComponent(mapper: ComponentMapper<T>): T = mapper.get(this)