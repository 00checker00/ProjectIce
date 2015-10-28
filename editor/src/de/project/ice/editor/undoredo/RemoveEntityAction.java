package de.project.ice.editor.undoredo;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;

public class RemoveEntityAction implements UndoableRedoableAction
{
    private Entity entity;
    private Engine engine;

    public RemoveEntityAction(Entity entity, Engine engine)
    {
        this.entity = entity;
        this.engine = engine;
    }


    @Override
    public void undo()
    {
        engine.addEntity(entity);
    }

    @Override
    public void redo()
    {
        engine.removeEntity(entity);
    }

}
