package de.project.ice.editor.undoredo;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;

public class AddEntityAction implements UndoableRedoableAction
{
    private Entity entity;
    private Engine engine;

    public AddEntityAction(Entity entity, Engine engine)
    {
        this.entity = entity;
        this.engine = engine;
    }


    @Override
    public void undo()
    {
        engine.removeEntity(entity);
    }

    @Override
    public void redo()
    {
        engine.addEntity(entity);
    }

}
