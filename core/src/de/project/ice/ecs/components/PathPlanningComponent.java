package de.project.ice.ecs.components;

import com.badlogic.gdx.math.Vector2;
import org.jetbrains.annotations.NotNull;

public class PathPlanningComponent implements IceComponent<PathPlanningComponent>
{
    @NotNull
    public Vector2 start = new Vector2(0f, 0f);
    @NotNull
    public Vector2 target = new Vector2(0f, 0f);

    @Override
    public void reset()
    {
        start = new Vector2();
        target = new Vector2();
    }

    @Override
    public void copyTo(PathPlanningComponent copy)
    {
        copy.start = start.cpy();
        copy.target = target.cpy();
    }
}
