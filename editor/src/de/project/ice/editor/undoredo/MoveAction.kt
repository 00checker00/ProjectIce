package de.project.ice.editor.undoredo

import com.badlogic.gdx.math.Vector2
import de.project.ice.ecs.components.TransformComponent

class MoveAction(private val posbefore: Vector2, private val posafter: Vector2, private val target: TransformComponent) : UndoableRedoableAction {

    override fun undo() {
        target.pos.set(posbefore)
    }

    override fun redo() {
        target.pos.set(posafter)
    }

}
