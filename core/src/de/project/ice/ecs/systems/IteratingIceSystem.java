package de.project.ice.ecs.systems;


import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import de.project.ice.ecs.IceEngine;

public abstract class IteratingIceSystem extends IteratingSystem
{
    public IteratingIceSystem(Family family)
    {
        super(family);
    }

    public IteratingIceSystem(Family family, int priority)
    {
        super(family, priority);
    }

    @Override
    @Deprecated
    public void addedToEngine(Engine engine)
    {
        addedToEngine((IceEngine) engine);
    }

    @Override
    @Deprecated
    public void removedFromEngine(Engine engine)
    {
        removedFromEngine((IceEngine) engine);
    }

    public void addedToEngine(IceEngine engine)
    {
        super.addedToEngine(engine);
    }

    public void removedFromEngine(IceEngine engine)
    {
        super.removedFromEngine(engine);
    }

    @Override
    public IceEngine getEngine()
    {
        return (IceEngine) super.getEngine();
    }
}
