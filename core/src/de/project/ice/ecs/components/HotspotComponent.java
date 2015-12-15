package de.project.ice.ecs.components;

import com.badlogic.gdx.math.Vector2;

public class HotspotComponent implements IceComponent<HotspotComponent>
{
    public Vector2 origin = new Vector2(0f, 0f);
    public Vector2 targetPos = new Vector2(0f, 0f);
    public float width;
    public float height;
    public String script;

    @Override
    public void reset()
    {
        origin = new Vector2();
        targetPos = new Vector2();
        width = 0f;
        height = 0;
        script = "";
    }

    @Override
    public void copyTo(HotspotComponent copy)
    {
        copy.origin.set(origin);
        copy.targetPos.set(targetPos);
        copy.width = width;
        copy.height = height;
        copy.script = script;
    }
}


