package de.project.ice.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

/**
 * Component for idle breathing animation of characters
 */
public class BreathComponent extends Component implements Pool.Poolable {

    public Vector2 curScale = new Vector2(0f, 0f);
    public Vector2 scaleValue = new Vector2(0f, 0f);
    public float duration = 1f;

    @Override
    public void reset(){
        curScale.set(0f, 0f);
        scaleValue.set(0f, 0f);
        duration = 1f;
    }
}


