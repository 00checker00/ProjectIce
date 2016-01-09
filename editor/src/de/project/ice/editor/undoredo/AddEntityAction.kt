package de.project.ice.editor.undoredo

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity

class AddEntityAction(private val entity: Entity, private val engine: Engine) : UndoableRedoableAction {


    override fun undo() {
        engine.removeEntity(entity)
    }

    override fun redo() {
        engine.addEntity(entity)
    }

}
