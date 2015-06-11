package de.project.ice.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Pool;
import de.project.ice.utils.Assets;
import org.jetbrains.annotations.NotNull;

public class AnimationComponent extends Component implements Pool.Poolable {
    @NotNull
    public IntMap<Assets.AnimationHolder> animations = new IntMap<Assets.AnimationHolder>();

    @Override
    public void reset () {
        animations.clear();
    }
}