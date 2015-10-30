package de.project.ice.ecs.components;

import com.badlogic.gdx.math.Vector2;

/**
 * Component for idle breathing animation of characters
 */
public class BreathComponent implements IceComponent
{

    public Vector2 curScale = new Vector2(0f, 0f);
    public Vector2 scaleValue = new Vector2(0f, 0f);
    public float duration = 1f;
    public float time = 0.0f;

    @Override
    public void reset()
    {
        curScale.set(0f, 0f);
        scaleValue.set(0f, 0f);
        duration = 1f;
        time = 0.0f;
    }
}


