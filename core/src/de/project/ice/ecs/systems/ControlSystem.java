package de.project.ice.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;
import de.project.ice.ecs.Components;
import de.project.ice.ecs.components.ControlComponent;
import de.project.ice.ecs.components.MovableComponent;
import org.jetbrains.annotations.NotNull;

public class ControlSystem extends IteratingSystem implements InputProcessor {
    @NotNull
    private Array<Entity> controlQueue = new Array<Entity>();

    private Vector3 pointerPos = new Vector3();
    private boolean pointerDown = false;
    private boolean pointerClicked = false;

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

            if(pointerDown) {
                if(pointerClicked) {
                    pointerClicked = false;

                    move.targetPositions.add(pointerPos); // the position of the mouse cursor
                }
            }
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        pointerPos.set(screenX, screenY, 0f);
        pointerDown = true;
        pointerClicked = true;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        pointerPos.set(screenX, screenY, 0f);
        pointerDown = false;
        pointerClicked = false;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        pointerPos.set(screenX, screenY, 0f);
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}