package de.project.ice.ecs

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.ashley.utils.ImmutableArray
import com.badlogic.gdx.Gdx
import de.project.ice.IceGame
import de.project.ice.ecs.components.NameComponent
import de.project.ice.ecs.components.TimeoutComponent
import de.project.ice.ecs.systems.*

class IceEngine(val game: IceGame) : PooledEngine() {
    val animationSystem: AnimationSystem by lazy { AnimationSystem() }
    val renderingSystem: RenderingSystem by lazy { RenderingSystem() }
    val cameraSystem: CameraSystem by lazy { CameraSystem() }
    val scriptingSystem: ScriptingSystem by lazy { ScriptingSystem() }
    val movementSystem: MovementSystem by lazy { MovementSystem() }
    val controlSystem: ControlSystem by lazy { ControlSystem() }
    val breathSystem: BreathSystem by lazy { BreathSystem() }
    val soundSystem: SoundSystem by lazy { SoundSystem() }
    val pathSystem: PathSystem by lazy { PathSystem() }
    val useSystem: UseSystem by lazy { UseSystem() }
    val distanceScaleSytem: DistanceScaleSystem by lazy { DistanceScaleSystem() }
    val timeoutSystem: TimeoutSystem by lazy { TimeoutSystem() }
    val reloadAssetsSystem: ReloadAssetsSystem by lazy { ReloadAssetsSystem() }


    private val namedEntities: ImmutableArray<Entity>

    init {
        addSystem(animationSystem)
        addSystem(cameraSystem)
        addSystem(renderingSystem)
        addSystem(scriptingSystem)
        addSystem(movementSystem)
        addSystem(controlSystem)
        addSystem(breathSystem)
        addSystem(soundSystem)
        addSystem(pathSystem)
        addSystem(useSystem)
        addSystem(distanceScaleSytem)
        addSystem(timeoutSystem)
        addSystem(reloadAssetsSystem)

        namedEntities = getEntitiesFor(Family.all(NameComponent::class.java).get())
    }

    fun getEntityByName(name: String): Entity? {
        for (entity in namedEntities) {
            if (name == Components.name.get(entity).name) {
                return entity
            }
        }

        return null
    }

    override fun addEntity(entity: Entity) {
        super.addEntity(entity)
        Gdx.app.log(javaClass.simpleName, "Added entity " + getEntityName(entity))
    }

    override fun removeEntity(entity: Entity) {
        super.removeEntity(entity)
        Gdx.app.log(javaClass.simpleName, "Removed entity " + getEntityName(entity))
    }

    fun timeout(time: Float, function: ()->Unit) {
        addEntity(createEntity().apply {
            add(createComponent(TimeoutComponent::class.java).apply {
                this.function = function
                this.timeout = time
            })
        })
    }

    companion object {

        fun getEntityName(entity: Entity): String {
            val nameComponent = Components.name.get(entity)
            if (nameComponent != null) {
                return nameComponent.name
            } else {
                return "Unnamed"
            }
        }
    }
}
