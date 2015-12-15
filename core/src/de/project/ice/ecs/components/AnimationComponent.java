package de.project.ice.ecs.components;

import com.badlogic.gdx.utils.IntMap;
import de.project.ice.utils.Assets;
import org.jetbrains.annotations.NotNull;

public class AnimationComponent implements IceComponent<AnimationComponent>
{
    public int animation = 0;
    public float time = 0.0f;

    @NotNull
    public IntMap<Assets.AnimationHolder> animations = new IntMap<Assets.AnimationHolder>();

    @Override
    public void reset()
    {
        animations = new IntMap<Assets.AnimationHolder>();
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

    @Override
    public void copyTo(AnimationComponent copy)
    {
        copy.animation = animation;
        for (IntMap.Entry<Assets.AnimationHolder> animationHolder : animations)
        {
            copy.animations.put(animationHolder.key,
                    Assets.createAnimation(
                            animationHolder.value.name,
                            animationHolder.value.data.getFrameDuration(),
                            animationHolder.value.data.getPlayMode()
                    )
            );
        }
        copy.time = time;
    }
}