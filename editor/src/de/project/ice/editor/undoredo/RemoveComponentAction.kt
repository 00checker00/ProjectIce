package de.project.ice.editor.undoredo

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity

class RemoveComponentAction(private val component: Component, private val entity: Entity) : UndoableRedoableAction {

    override fun undo() {
        entity.add(component)
    }

    override fun redo() {
        entity.remove(component.javaClass)
    }

}
