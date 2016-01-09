package de.project.ice.editor.undoredo

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity

class RemoveEntityAction(private val entity: Entity, private val engine: Engine) : UndoableRedoableAction {

    override fun undo() {
        engine.addEntity(entity)
    }

    override fun redo() {
        engine.removeEntity(entity)
    }

}
