package de.project.ice.ecs.components;

public class WalkingComponent implements IceComponent<WalkingComponent>
{
    public boolean isWalking = false;
    public int animation = 0;

    @Override
    public void reset()
    {
        isWalking = false;
        animation = 0;
    }

    @Override
    public void copyTo(WalkingComponent copy)
    {
        copy.isWalking = isWalking;
        copy.animation = animation;
    }
}
