package de.project.ice.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import de.project.ice.ecs.Components;
import de.project.ice.ecs.components.StateComponent;

public class StateSystem extends IteratingSystem {

    @SuppressWarnings("unchecked")
    public StateSystem () {
        super(Family.all(StateComponent.class).get());
    }

    @Override
    public void processEntity (Entity entity, float deltaTime) {
        Components.state.get(entity).time += deltaTime;
    }
}