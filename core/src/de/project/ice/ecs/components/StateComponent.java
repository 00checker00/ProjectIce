package de.project.ice.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class StateComponent extends Component implements Pool.Poolable {
    private int state = 0;
    public float time = 0.0f;

    public int get () {
        return state;
    }

    public void set (int newState) {
        state = newState;
        time = 0.0f;
    }

    @Override
    public void reset () {
        state = 0;
        time = 0.0f;
    }
}
