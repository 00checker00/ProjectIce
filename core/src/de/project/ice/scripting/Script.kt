package de.project.ice.scripting

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.reflect.ClassReflection
import com.badlogic.gdx.utils.reflect.ReflectionException
import de.project.ice.IceGame
import de.project.ice.ecs.IceEngine
import de.project.ice.hotspot.Hotspot
import de.project.ice.hotspot.HotspotLoader

/**
 * Base class for scripts
 * Note that all scripts have to be inside the "de.project.ice.hotspot.hotspots.scene1panorama.scripts" package
 */
abstract class Script {
    /**
     * Call once every cycle after all onUpdateEntity

     * @param delta the delta time in seconds
     */
    fun onUpdate(game: IceGame, delta: Float) {
    }

    /**
     * Call every cycle for every Entity

     * @param delta the delta time in seconds
     */
    open fun onUpdateEntity(game: IceGame, entity: Entity, delta: Float) {
    }

    /**
     * Called when this script is attached to an Entity

     * @param entity the entity the script has been attached to
     */
    open fun onAttachedToEntity(game: IceGame, entity: Entity) {
    }

    /**
     * Called when the Entity this script is attached to has been removed from the engine

     * @param entity the entity which has been removed
     */
    open fun onAttachedEntityRemoved(game: IceGame, entity: Entity) {
    }

    /**
     * Called approximately every second
     */
    fun onTick(game: IceGame) {
    }

    companion object {
        private var _loader: ScriptLoader? = null
        private val loader: ScriptLoader
            get() = _loader ?: ScriptLoader().apply { _loader = this }
        private val scripts = ObjectMap<String, Script>()

        operator fun get(id: String): Script? {
            var script: Script? = scripts.get(id, null)
            if (script == null) {
                val classname = "de.project.ice.hotspot.hotspots.scene1panorama.scripts.$id"
                try {
                    val clazz = loader.loadClass(classname)
                    script = clazz.newInstance() as? Script
                } catch (ex: Exception) {
                    println("Error while loading script with id: $id")
                    script = null
                }
                scripts.put(id, script)
            }
            return script
        }

        fun reloadScripts() {
            _loader = null
            scripts.clear()
        }
    }
}
