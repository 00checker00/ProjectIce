package de.project.ice.editor.undoredo

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import de.project.ice.ecs.IceEngine
import de.project.ice.ecs.components.CopyableIceComponent
import de.project.ice.ecs.components.IceComponent

class DuplicateEntityAction(entity: Entity, engine: IceEngine) : UndoableRedoableAction {
    private val duplicate: Entity
    private val engine: Engine

    init {
        this.engine = engine

        duplicate = engine.createEntity()
        for (component in entity.components) {
            val iceComponent = component as IceComponent

            if (iceComponent is CopyableIceComponent) {
                val dupeComponent = engine.createComponent(iceComponent.javaClass)

                iceComponent.copyTo(dupeComponent)

                duplicate.add(dupeComponent)
            }
        }
    }


    override fun undo() {
        engine.removeEntity(duplicate)
    }

    override fun redo() {
        engine.addEntity(duplicate)
    }

}
