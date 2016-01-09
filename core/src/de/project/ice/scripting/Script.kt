package de.project.ice.scripting

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.reflect.ClassReflection
import com.badlogic.gdx.utils.reflect.ReflectionException
import de.project.ice.IceGame
import de.project.ice.ecs.IceEngine

/**
 * Base class for scripts
 * Note that all scripts have to be inside the "de.project.ice.scripting.scripts" package
 */
abstract class Script {
    private var engine: IceEngine? = null
    private var game: IceGame? = null

    /**
     * Call once every cycle after all onUpdateEntity

     * @param delta the delta time in seconds
     */
    fun onUpdate(delta: Float) {
    }

    /**
     * Call every cycle for every Entity

     * @param delta the delta time in seconds
     */
    open fun onUpdateEntity(entity: Entity, delta: Float) {
    }

    /**
     * Called when this script is attached to an Entity

     * @param entity the entity the script has been attached to
     */
    open fun onAttachedToEntity(entity: Entity) {
    }

    /**
     * Called when the Entity this script is attached to has been removed from the engine

     * @param entity the entity which has been removed
     */
    open fun onAttachedEntityRemoved(entity: Entity) {
    }

    /**
     * Called approximately every second
     */
    fun onTick() {
    }

    @Throws(IllegalStateException::class)
    fun Engine(): IceEngine {
        if (engine == null) {
            throw IllegalStateException("An unloaded script tried to access the Engine")
        }
        return engine!!
    }

    @Throws(IllegalStateException::class)
    fun Game(): IceGame {
        if (game == null) {
            throw IllegalStateException("An unloaded script tried to access the Game")
        }
        return game!!
    }

    companion object {

        fun loadScript(scriptName: String, game: IceGame): Script? {
            try {
                val clazz = ClassReflection.forName("de.project.ice.scripting.scripts." + scriptName)
                val script = ClassReflection.newInstance<Script>(clazz as Class<Script>?)
                script.engine = game.engine
                script.game = game
                return script
            } catch (e: ReflectionException) {
                Gdx.app.log(Script::class.java.simpleName, "Unable to load script: " + scriptName)
            }

            return null
        }
    }
}
