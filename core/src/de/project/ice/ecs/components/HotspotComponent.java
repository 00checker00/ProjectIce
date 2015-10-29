package de.project.ice.ecs.components;

import com.badlogic.gdx.math.Vector2;

public class HotspotComponent implements IceComponent
{
    public Vector2 origin = new Vector2(0f, 0f);
    public Vector2 targetPos = new Vector2(0f, 0f);
    public float width;
    public float height;
    public String script;

    @Override
    public void reset()
    {
        origin.set(0f, 0f);
        targetPos.set(0f, 0f);
        width = 0f;
        height = 0;
        script = "";
    }
}


