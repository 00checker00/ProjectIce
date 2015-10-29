package de.project.ice.ecs.components;

import de.project.ice.ecs.systems.AnimationSystem;

public class StateComponent implements IceComponent
{
    public int animation = AnimationSystem.ANIMATION_DEFAULT;
    public float time = 0.0f;

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
    public void reset()
    {
        animation = AnimationSystem.ANIMATION_DEFAULT;
        time = 0.0f;
    }
}
