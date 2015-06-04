package de.project.ice.ecs.systems;


import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.systems.IntervalSystem;
import de.project.ice.ecs.IceEngine;

public abstract class IntervalIceSystem extends IntervalSystem {
    public IntervalIceSystem(float interval) {
        super(interval);
    }

    public IntervalIceSystem(float interval, int priority) {
        super(interval, priority);
    }

    @Override
    @Deprecated
    public void addedToEngine(Engine engine) {
        addedToEngine((IceEngine) engine);
    }

    @Override
    @Deprecated
    public void removedFromEngine(Engine engine) {
        removedFromEngine((IceEngine) engine);
    }

    public void addedToEngine(IceEngine engine) {
        super.addedToEngine(engine);
    }

    public void removedFromEngine(IceEngine engine) {
        super.removedFromEngine(engine);
    }
}
