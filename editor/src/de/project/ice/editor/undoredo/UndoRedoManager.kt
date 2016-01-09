package de.project.ice.editor.undoredo

import com.badlogic.gdx.utils.Array
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty

class UndoRedoManager {
    private val actions = Array<UndoableRedoableAction>()
    private var currentPosition: Int = 0
    private val canUndo = SimpleBooleanProperty(false)
    private val canRedo = SimpleBooleanProperty(false)

    fun addAction(action: UndoableRedoableAction) {
        for (i in actions.size - 1 downTo currentPosition) {
            actions.removeIndex(i)
        }
        actions.add(action)
        currentPosition++
        canUndo.set(canUndo())
        canRedo.set(canRedo())
        action.redo()
    }

    fun undo() {
        if (currentPosition > 0) {
            currentPosition--
            actions.get(currentPosition).undo()
            canUndo.set(canUndo())
            canRedo.set(canRedo())
        }
    }

    fun redo() {
        if (currentPosition < actions.size) {
            actions.get(currentPosition).redo()
            currentPosition++
            canUndo.set(canUndo())
            canRedo.set(canRedo())
        }
    }

    fun canUndo(): Boolean {
        return currentPosition > 0
    }

    fun canRedo(): Boolean {
        return currentPosition < actions.size
    }

    fun canUndoProperty(): BooleanProperty {
        return canUndo
    }

    fun canRedoProperty(): BooleanProperty {
        return canRedo
    }

}
