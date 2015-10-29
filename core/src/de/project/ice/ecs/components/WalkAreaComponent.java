package de.project.ice.ecs.components;

import de.project.ice.pathlib.PathArea;
import org.jetbrains.annotations.NotNull;

public class WalkAreaComponent implements IceComponent
{
    @NotNull
    public PathArea area = new PathArea();

    @Override
    public void reset()
    {
        area = new PathArea();
    }
}
