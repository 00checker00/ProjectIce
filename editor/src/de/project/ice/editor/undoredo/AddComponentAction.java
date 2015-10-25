package de.project.ice.editor.undoredo;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class AddComponentAction implements UndoableRedoableAction
{

    private Component component;
    private Entity entity;

    public AddComponentAction(Component component, Entity entity)
    {
        this.component = component;
        this.entity = entity;
    }

    @Override
    public void undo()
    {
        entity.remove(component.getClass());
    }

    @Override
    public void redo()
    {
        entity.add(component);
    }

}
