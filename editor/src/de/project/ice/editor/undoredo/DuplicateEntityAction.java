package de.project.ice.editor.undoredo;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import de.project.ice.ecs.IceEngine;
import de.project.ice.ecs.components.IceComponent;

public class DuplicateEntityAction implements UndoableRedoableAction
{
    private Entity duplicate;
    private Engine engine;

    public DuplicateEntityAction(Entity entity, IceEngine engine)
    {
        this.engine = engine;

        duplicate = engine.createEntity();
        for (Component component : entity.getComponents())
        {
            IceComponent iceComponent = (IceComponent) component;
            IceComponent dupeComponent = engine.createComponent(iceComponent.getClass());

            iceComponent.copyTo(dupeComponent);

            duplicate.add(dupeComponent);
        }
    }


    @Override
    public void undo()
    {
        engine.removeEntity(duplicate);
    }

    @Override
    public void redo()
    {
        engine.addEntity(duplicate);
    }

}
