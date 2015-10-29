package de.project.ice.ecs.components;

import com.badlogic.gdx.math.Vector2;
import org.jetbrains.annotations.NotNull;

public class TransformComponent implements IceComponent
{
    @NotNull
    public Vector2 pos = new Vector2();
    @NotNull
    public Vector2 scale = new Vector2(1.0f, 1.0f);
    public float rotation = 0.0f;
    public int z = 0;
    public boolean flipHorizontal;
    public boolean flipVertical;

    @Override
    public void reset()
    {
        pos.set(new Vector2());
        scale.set(1.0f, 1.0f);
        rotation = 0.0f;
        z = 0;
        flipHorizontal = false;
        flipVertical = false;
    }
}
