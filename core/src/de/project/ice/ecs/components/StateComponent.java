package de.project.ice.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import de.project.ice.ecs.systems.AnimationSystem;

public class StateComponent extends Component implements Pool.Poolable {
    private int animation = AnimationSystem.ANIMATION_NONE;
    public float time = 0.0f;

    public int getAnimation() {
        return animation;
    }

    public void setAnimation(int animation) {
        this.animation = animation;
        time = 0.0f;
    }

    @Override
    public void reset () {
        animation = 0;
        time = 0.0f;
    }
}
