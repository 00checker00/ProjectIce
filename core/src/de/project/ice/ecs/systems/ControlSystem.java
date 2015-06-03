package de.project.ice.ecs.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import de.project.ice.ecs.Components;
import de.project.ice.ecs.Families;
import de.project.ice.ecs.components.CameraComponent;
import de.project.ice.ecs.components.ControlComponent;
import de.project.ice.ecs.components.MovableComponent;
import de.project.ice.ecs.components.TransformComponent;
import org.jetbrains.annotations.NotNull;

public class ControlSystem extends IteratingSystem {

    @NotNull
    private Array<Entity> controlQueue = new Array<Entity>();

    @SuppressWarnings("unchecked")
    public ControlSystem() {
        super(Family.all(ControlComponent.class, MovableComponent.class).get());
    }

    @Override
    public void processEntity (Entity entity, float deltaTime) {
        controlQueue.add(entity);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        for(Entity entity : controlQueue)
        {

            MovableComponent move = Components.movable.get(entity);
            ControlComponent control = Components.control.get(entity);

            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                if(!control.isMousePressed) {
                    //Gdx.app.log("Mouse clicked", "");
                    move.targetPositions.add(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f)); // the position of the mouse cursor
                    control.isMousePressed = true;
                }
            }
            else {
                control.isMousePressed = false;
            }

        }
    }


}