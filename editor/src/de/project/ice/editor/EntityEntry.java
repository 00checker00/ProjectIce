package de.project.ice.editor;

import com.badlogic.ashley.core.Entity;
import de.project.ice.ecs.Components;
import de.project.ice.ecs.components.NameComponent;

class EntityEntry
{
    public Entity entity;
    public String name;

    public EntityEntry(Entity entity)
    {
        this.entity = entity;
        this.name = generateName(entity);
    }

    @Override
    public String toString()
    {
        return name;
    }

    public static String generateName(Entity entity)
    {
        NameComponent nameComponent = Components.name.get(entity);
        if (nameComponent != null)
        {
            return nameComponent.name;
        }
        else
        {
            return "Unnamed(0x" + Long.toHexString(entity.getId()) + ")";
        }
    }
}
