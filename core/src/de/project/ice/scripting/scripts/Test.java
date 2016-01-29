package de.project.ice.scripting.scripts;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import de.project.ice.ecs.Components;
import de.project.ice.ecs.components.PathPlanningComponent;
import de.project.ice.ecs.components.TransformComponent;
import de.project.ice.scripting.Script;
import org.jetbrains.annotations.NotNull;

public class Test extends Script
{
    @Override
    public void onAttachedToEntity(@NotNull Entity entity)
    {
        PathPlanningComponent pc = null;

        if (Components.transform.has(entity))
        {
            TransformComponent transformComponent = Components.transform.get(entity);
            Vector2 start = transformComponent.getPos();

            pc.setTarget(new Vector2(2.0f, 2.0f));
            pc.setStart(start);
        }
    }
}
