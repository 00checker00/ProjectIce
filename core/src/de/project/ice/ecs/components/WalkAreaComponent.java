package de.project.ice.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import de.project.ice.pathlib.PathArea;
import org.jetbrains.annotations.NotNull;

public class WalkAreaComponent extends Component implements Pool.Poolable
{
    @NotNull
    public PathArea area = new PathArea();

    @Override
    public void reset()
    {
        area = new PathArea();
    }
}
