package de.project.ice.editor.undoredo;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

public class RemoveComponentAction implements UndoableRedoableAction {

    private Component component;
    private Entity entity;

    public RemoveComponentAction (Component component, Entity entity) {
        this.component = component;
        this.entity = entity;
    }

    @Override
    public void undo () {
        entity.add(component);
    }

    @Override
    public void redo () {
        entity.remove(component.getClass());
    }

}
