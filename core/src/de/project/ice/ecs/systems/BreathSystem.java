package de.project.ice.ecs.systems;

import com.badlogic.ashley.core.Entity;
import de.project.ice.ecs.Components;
import de.project.ice.ecs.Families;
import de.project.ice.ecs.components.BreathComponent;
import de.project.ice.ecs.components.StateComponent;

public class BreathSystem extends IteratingIceSystem
{
    public BreathSystem()
    {
        super(Families.breathing);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime)
    {
        StateComponent state = Components.state.get(entity);
        BreathComponent breath = Components.breath.get(entity);
        breath.curScale.set(breath.scaleValue.cpy().scl((float) Math.sin(state.time / breath.duration)));
    }
}
