package de.project.ice.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

/**
 * Component for idle breathing animation of characters
 */
public class BreathComponent extends Component implements Pool.Poolable {

    public final Vector2 scale = new Vector2(1.0f, 1.0f);
    public final float speed = 30f;

    @Override
    public void reset(){
        scale.set(1.0f, 1.0f);
    }
}


