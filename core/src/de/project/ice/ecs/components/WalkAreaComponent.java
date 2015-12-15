package de.project.ice.ecs.components;

import com.badlogic.gdx.Gdx;
import de.project.ice.pathlib.PathArea;
import org.jetbrains.annotations.NotNull;

public class WalkAreaComponent implements IceComponent<WalkAreaComponent>
{
    @NotNull
    public PathArea area = new PathArea();

    @Override
    public void reset()
    {
        area = new PathArea();
    }

    @Override
    public void copyTo(WalkAreaComponent copy)
    {
        Gdx.app.log(getClass().getSimpleName(), "WalkAreaComponent is not copyable");
    }
}
