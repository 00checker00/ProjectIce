package de.project.ice.ecs.components;

import com.badlogic.gdx.math.Vector2;

import java.util.LinkedList;
import java.util.List;

/**
 * Component for movable objects and characters
 */
public class MovableComponent implements IceComponent
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
