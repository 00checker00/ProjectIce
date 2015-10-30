package de.project.ice.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import de.project.ice.ecs.Components;
import de.project.ice.ecs.components.HotspotComponent;
import de.project.ice.ecs.components.MoveComponent;
import de.project.ice.ecs.components.UseComponent;
import de.project.ice.hotspot.HotspotManager;

public class UseSystem extends IteratingIceSystem
{
    @SuppressWarnings("unchecked")
    public UseSystem()
    {
        super(Family.all(UseComponent.class).exclude(MoveComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime)
    {
        UseComponent component = Components.use.get(entity);
        if (component.target == null ||
                !Components.transform.has(component.target) ||
                !Components.hotspot.has(component.target))
        {
            throw new RuntimeException("Target is invalid");
        }
        HotspotComponent hotspotComponent = Components.hotspot.get(component.target);

        HotspotManager.Hotspot hotspot = getEngine().game.hotspotManager.get(hotspotComponent.script);

        if (hotspot != null)
        {
            if (component.withItem != null)
            {
                hotspot.useWith(component.withItem);
            }
            else
            {
                hotspot.use(component.cursor);
            }
        }
        entity.remove(UseComponent.class);
    }
}
