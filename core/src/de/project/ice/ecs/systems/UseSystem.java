package de.project.ice.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import de.project.ice.ecs.Components;
import de.project.ice.ecs.components.HotspotComponent;
import de.project.ice.ecs.components.MoveComponent;
import de.project.ice.ecs.components.UseComponent;
import de.project.ice.hotspot.Hotspot;
import de.project.ice.hotspot.Hotspots;

public class UseSystem extends IteratingIceSystem
{
    public UseSystem()
    {
        super(Family.all(UseComponent.class).exclude(MoveComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime)
    {
        UseComponent use = Components.use.get(entity);

        if (use.target == null ||
                !Components.hotspot.has(use.target))
        {
            throw new RuntimeException("Target is invalid");
        }
        HotspotComponent hotspotComponent = Components.hotspot.get(use.target);


        Hotspot hotspot = Hotspots.get(hotspotComponent.script);

        if (hotspot != null)
        {
            if (use.withItem != null)
            {
                hotspot.useWith(getEngine().game, use.withItem.name);
            }
            else
            {
                hotspot.use(getEngine().game, use.cursor);
            }
        }
        entity.remove(UseComponent.class);
    }
}
