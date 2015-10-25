package de.project.ice.editor.undoredo;

import com.badlogic.gdx.utils.Array;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class UndoRedoManager
{
    private Array<UndoableRedoableAction> actions;

    private int currentPosition;

    private SimpleBooleanProperty canUndo = new SimpleBooleanProperty(false);

    private SimpleBooleanProperty canRedo = new SimpleBooleanProperty(false);

    public UndoRedoManager()
    {
        actions = new Array<UndoableRedoableAction>();
        currentPosition = 0;
    }

    public void addAction(UndoableRedoableAction action)
    {
        for (int i = actions.size - 1; i >= currentPosition; i--)
        {
            actions.removeIndex(i);
        }
        actions.add(action);
        currentPosition++;
        canUndo.set(canUndo());
        canRedo.set(canRedo());
        action.redo();
    }

    public void undo()
    {
        if (currentPosition > 0)
        {
            currentPosition--;
            actions.get(currentPosition).undo();
            canUndo.set(canUndo());
            canRedo.set(canRedo());
        }
    }

    public void redo()
    {
        if (currentPosition < actions.size)
        {
            actions.get(currentPosition).redo();
            currentPosition++;
            canUndo.set(canUndo());
            canRedo.set(canRedo());
        }
    }

    public boolean canUndo()
    {
        return currentPosition > 0;
    }

    public boolean canRedo()
    {
        return currentPosition < actions.size;
    }

    public BooleanProperty canUndoProperty()
    {
        return canUndo;
    }

    public BooleanProperty canRedoProperty()
    {
        return canRedo;
    }

}
