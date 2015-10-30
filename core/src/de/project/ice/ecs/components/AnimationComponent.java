package de.project.ice.ecs.components;

import com.badlogic.gdx.utils.IntMap;
import de.project.ice.utils.Assets;
import org.jetbrains.annotations.NotNull;

public class AnimationComponent implements IceComponent
{
    public int animation = 0;
    public float time = 0.0f;

    @NotNull
    public IntMap<Assets.AnimationHolder> animations = new IntMap<Assets.AnimationHolder>();

    @Override
    public void reset()
    {
        animations.clear();
        animation = 0;
        time = 0.0f;
    }

    public int getAnimation()
    {
        return animation;
    }

    public void setAnimation(int animation)
    {
        this.animation = animation;
        time = 0.0f;
    }
}