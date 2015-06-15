package de.project.ice.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class HotspotComponent extends Component implements Pool.Poolable {
    public Vector2 origin = new Vector2(0f, 0f);
    public float width = 0f;
    public float height = 0f;

    @Override
    public void reset(){
        origin.set(0f, 0f);
        width = 0f;
        height = 0;
    }
}


