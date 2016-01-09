package de.project.ice.editor.undoredo

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity

class AddComponentAction(private val component: Component, private val entity: Entity) : UndoableRedoableAction {

    override fun undo() {
        entity.remove(component.javaClass)
    }

    override fun redo() {
        entity.add(component)
    }

}
