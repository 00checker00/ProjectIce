package de.project.ice.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Pool;
import org.jetbrains.annotations.NotNull;

public class AnimationComponent extends Component implements Pool.Poolable {
    @NotNull
    public IntMap<Animation> animations = new IntMap<Animation>();

    @Override
    public void reset () {
        animations.clear();
    }
}