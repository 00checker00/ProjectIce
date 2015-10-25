package de.project.ice.editor.undoredo;

import com.badlogic.gdx.math.Vector2;
import de.project.ice.ecs.components.TransformComponent;

public class MoveAction implements UndoableRedoableAction
{
    private Vector2 posbefore;
    private Vector2 posafter;
    private TransformComponent target;

    public MoveAction(Vector2 posbefore, Vector2 posafter, TransformComponent target)
    {
        this.posbefore = posbefore;
        this.posafter = posafter;
        this.target = target;
    }

    @Override
    public void undo()
    {
        target.pos.set(posbefore);
    }

    @Override
    public void redo()
    {
        target.pos.set(posafter);
    }

}
