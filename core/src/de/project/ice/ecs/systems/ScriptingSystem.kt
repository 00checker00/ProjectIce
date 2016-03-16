package de.project.ice.ecs.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.utils.ImmutableArray
import de.project.ice.ecs.Components
import de.project.ice.ecs.Families
import de.project.ice.ecs.IceEngine
import de.project.ice.scripting.Script
import java.util.*

class ScriptingSystem() : IntervalIceSystem(1f), EntityListener {
    internal var activeScripts = HashSet<Script>()
    private val family = Families.scripted
    private var entities = ImmutableArray<Entity>(com.badlogic.gdx.utils.Array(0))

    override fun removedFromEngine(engine: Engine) {
        super.removedFromEngine(engine as IceEngine)
        engine.removeEntityListener(this)
    }

    override fun addedToEngine(engine: IceEngine) {
        super.addedToEngine(engine)
        entities = engine.getEntitiesFor(family)
        engine.addEntityListener(family, this)
    }

    override fun update(deltaTime: Float) {
        activeScripts.clear()
        for (entity in entities) {
            val component = Components.script.get(entity)
            val script = component.script
            if (script == null) {
                component.script = Script[component.scriptName]
                if (component.script != null) {
                    component.script!!.onAttachedToEntity(engine!!.game, entity)
                }
            } else {
                activeScripts.add(script)
                script.onUpdateEntity(engine!!.game, entity, deltaTime)
            }
        }
        for (script in activeScripts) {
            script.onUpdate(engine!!.game, deltaTime)
        }
        super.update(deltaTime)
    }

    override fun updateInterval() {
        for (script in activeScripts) {
            script.onTick(engine!!.game)
        }
    }

    override fun entityAdded(entity: Entity) {

    }

    override fun entityRemoved(entity: Entity) {
        if (Components.script.has(entity)) {
            val script = Components.script.get(entity).script
            script?.onAttachedEntityRemoved(engine!!.game, entity)
        }
    }
}
