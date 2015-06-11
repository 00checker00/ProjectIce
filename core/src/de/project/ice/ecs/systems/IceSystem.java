package de.project.ice.ecs.systems;


import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import de.project.ice.ecs.IceEngine;

public class IceSystem extends EntitySystem {

    public IceSystem() {
    }

    public IceSystem(int priority) {
        super(priority);
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
        super.removedFromEngine(engine);
    }

    public void removedFromEngine(IceEngine engine) {
        super.addedToEngine(engine);
    }
}
