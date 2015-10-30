package de.project.ice.ecs.components;

import com.badlogic.gdx.math.Vector2;
import org.jetbrains.annotations.NotNull;

public class PathPlanningComponent implements IceComponent
{
    @NotNull
    public Vector2 start = new Vector2(0f, 0f);
    @NotNull
    public Vector2 target = new Vector2(0f, 0f);

    @Override
    public void reset()
    {
        start.set(0f, 0f);
        target.set(0f, 0f);
    }
}
