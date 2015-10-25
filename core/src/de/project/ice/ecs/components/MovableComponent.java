package de.project.ice.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

import java.util.LinkedList;
import java.util.List;

/**
 * Component for movable objects and characters
 */
public class MovableComponent extends Component implements Pool.Poolable
{
    public boolean isMoving = false;
    public List<Vector2> targetPositions = new LinkedList<Vector2>();
    public float speed = 2.0f;

    @Override
    public void reset()
    {
        isMoving = false;
        speed = 2.0f;
        targetPositions.clear();
    }
}
