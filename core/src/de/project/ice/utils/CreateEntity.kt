package de.project.ice.utils

import com.badlogic.ashley.core.Entity
import de.project.ice.ecs.IceEngine
import de.project.ice.ecs.components.*

fun IceEngine.addEntity(init: EntityBuilder.()->Unit)
    = createEntity(init).apply { addEntity(this) }

fun IceEngine.createEntity(init: EntityBuilder.()->Unit)
    = EntityBuilder(this).apply { init() }.entity

fun IceEngine.editEntity(entityName: String, init: EntityBuilder.()->Unit)
    = EntityBuilder(this, getEntityByName(entityName)?:createEntity()).apply { init() }.entity

fun Entity.editComponents(engine: IceEngine, init: EntityBuilder.()->Unit)
    = EntityBuilder(engine, this).apply { init() }.entity

class EntityBuilder {
    val entity: Entity
    val engine: IceEngine

    internal constructor(engine: IceEngine, entity: Entity = engine.createEntity()) {
        this.engine = engine
        this.entity = entity
    }

    fun removeComponent(comp: Class<out IceComponent>) {
        entity.remove(comp)
    }

    fun AnimationComponent(init: AnimationComponent.()->Unit)
            = let { val comp = engine.createComponent(AnimationComponent::class.java); comp.init(); entity.add(comp); comp }

    fun BlendComponent(init: BlendComponent.()->Unit)
            = let { val comp = engine.createComponent(BlendComponent::class.java); comp.init(); entity.add(comp); comp }

    fun BreathComponent(init: BreathComponent.()->Unit)
            = let { val comp = engine.createComponent(BreathComponent::class.java); comp.init(); entity.add(comp); comp }

    fun CameraComponent(init: CameraComponent.()->Unit)
            = let { val comp = engine.createComponent(CameraComponent::class.java); comp.init(); entity.add(comp); comp }

    fun ControlComponent(init: ControlComponent.()->Unit)
            = let { val comp = engine.createComponent(ControlComponent::class.java); comp.init(); entity.add(comp); comp }

    fun DisabledComponent(init: DisabledComponent.()->Unit)
            = let { val comp = engine.createComponent(DisabledComponent::class.java); comp.init(); entity.add(comp); comp }

    fun DistanceScaleComponent(init: DistanceScaleComponent.()->Unit)
            = let { val comp = engine.createComponent(DistanceScaleComponent::class.java); comp.init(); entity.add(comp); comp }

    fun HotspotComponent(init: HotspotComponent.()->Unit)
            = let { val comp = engine.createComponent(HotspotComponent::class.java); comp.init(); entity.add(comp); comp }

    fun IdleComponent(init: IdleComponent.()->Unit)
            = let { val comp = engine.createComponent(IdleComponent::class.java); comp.init(); entity.add(comp); comp }

    fun InvisibilityComponent(init: InvisibilityComponent.()->Unit)
            = let { val comp = engine.createComponent(InvisibilityComponent::class.java); comp.init(); entity.add(comp); comp }

    fun MoveComponent(init: MoveComponent.()->Unit)
            = let { val comp = engine.createComponent(MoveComponent::class.java); comp.init(); entity.add(comp); comp }

    fun NameComponent(init: NameComponent.()->Unit)
            = let { val comp = engine.createComponent(NameComponent::class.java); comp.init(); entity.add(comp); comp }

    fun PathPlanningComponent(init: PathPlanningComponent.()->Unit)
            = let { val comp = engine.createComponent(PathPlanningComponent::class.java); comp.init(); entity.add(comp); comp }

    fun ScriptComponent(init: ScriptComponent.()->Unit)
            = let { val comp = engine.createComponent(ScriptComponent::class.java); comp.init(); entity.add(comp); comp }

    fun TextureComponent(init: TextureComponent.()->Unit)
            = let { val comp = engine.createComponent(TextureComponent::class.java); comp.init(); entity.add(comp); comp }

    fun TimeoutComponent(init: TimeoutComponent.()->Unit)
            = let { val comp = engine.createComponent(TimeoutComponent::class.java); comp.init(); entity.add(comp); comp }

    fun TransformComponent(init: TransformComponent.()->Unit)
            = let { val comp = engine.createComponent(TransformComponent::class.java); comp.init(); entity.add(comp); comp }

    fun UseComponent(init: UseComponent.()->Unit)
            = let { val comp = engine.createComponent(UseComponent::class.java); comp.init(); entity.add(comp); comp }

    fun WalkAreaComponent(init: WalkAreaComponent.()->Unit)
            = let { val comp = engine.createComponent(WalkAreaComponent::class.java); comp.init(); entity.add(comp); comp }

    fun WalkingComponent(init: WalkingComponent.()->Unit)
            = let { val comp = engine.createComponent(WalkingComponent::class.java); comp.init(); entity.add(comp); comp }

    fun SpeakComponent(init: SpeakComponent.()->Unit)
            = let { val comp = engine.createComponent(SpeakComponent::class.java); comp.init(); entity.add(comp); comp }
}



