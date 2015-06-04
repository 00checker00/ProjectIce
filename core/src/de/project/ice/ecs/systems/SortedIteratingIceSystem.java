package de.project.ice.ecs.systems;


import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import de.project.ice.ecs.IceEngine;

import java.util.Comparator;

public abstract class SortedIteratingIceSystem extends SortedIteratingSystem {
    public SortedIteratingIceSystem(Family family, Comparator<Entity> comparator) {
        super(family, comparator);
    }

    public SortedIteratingIceSystem(Family family, Comparator<Entity> comparator, int priority) {
        super(family, comparator, priority);
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
