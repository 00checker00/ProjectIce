package de.project.ice.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Component for movable objects and characters
 *
 */
public class MovableComponent extends Component implements Pool.Poolable {

    // target position vector for character movement
    public Vector2 targetUnprojected = new Vector2();
    public Vector2 directionVector = new Vector2();
    public Vector2 movementVector = new Vector2();
    public Vector2 velocityVector = new Vector2();
    public boolean isMoving = false;

    public List<Vector3> targetPositions = new LinkedList<Vector3>();

    @Override
    public void reset () {
        targetUnprojected.set(new Vector2());
        directionVector.set(new Vector2());
        movementVector.set(new Vector2());
        velocityVector.set(new Vector2());
        isMoving = false;
        targetPositions.clear();
    }
}
