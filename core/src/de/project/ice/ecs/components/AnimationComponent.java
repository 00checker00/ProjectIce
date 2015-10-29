package de.project.ice.ecs.components;

import com.badlogic.gdx.utils.IntMap;
import de.project.ice.utils.Assets;
import org.jetbrains.annotations.NotNull;

public class AnimationComponent implements IceComponent
{
    @NotNull
    public IntMap<Assets.AnimationHolder> animations = new IntMap<Assets.AnimationHolder>();

    @Override
    public void reset()
    {
        animations.clear();
    }
}