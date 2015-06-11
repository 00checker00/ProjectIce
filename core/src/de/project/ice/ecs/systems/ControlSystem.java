package de.project.ice.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.project.ice.ecs.Components;
import de.project.ice.ecs.Families;
import de.project.ice.ecs.IceEngine;
import de.project.ice.ecs.components.CameraComponent;
import de.project.ice.ecs.components.ControlComponent;
import de.project.ice.ecs.components.MovableComponent;
import de.project.ice.ecs.components.TextureComponent;

public class ControlSystem extends IteratingIceSystem implements InputProcessor {

    private ImmutableArray<Entity> cameras;
    OrthographicCamera active_camera = null;

    private Vector2 pointerPos = new Vector2();
    private boolean pointerDown = false;
    private boolean pointerClicked = false;
    private CameraSystem cameraSystem;

    @SuppressWarnings("unchecked")
    public ControlSystem() {
        super(Family.all(ControlComponent.class, MovableComponent.class, TextureComponent.class).get());
    }

    @Override
    public void processEntity (Entity entity, float deltaTime) {
        MovableComponent move = Components.movable.get(entity);
        ControlComponent control = Components.control.get(entity);
        TextureComponent texture = Components.texture.get(entity);

        if(pointerDown) {
            if(pointerClicked) {
                pointerClicked = false;
                if (active_camera != null) {
                    Vector3 pos = active_camera.unproject(new Vector3(pointerPos.x, pointerPos.y, 0f)); // unprojects UI coordinates to camera coordinates
                    float width = RenderingSystem.PIXELS_TO_METRES * (texture.region.data != null ? texture.region.data.getRegionWidth() : 0);

                    move.targetPositions.clear();
                    move.targetPositions.add(new Vector2(pos.x - width/2, pos.y));
                }
            }

        }
    }

    @Override
    public void update(float deltaTime) {
        // update the active camera
        if(cameras.size() > 0)
            active_camera = cameras.first().getComponent(CameraComponent.class).camera;

        if (active_camera == null)
            return;

        super.update(deltaTime);
    }

    @Override
    public void addedToEngine(IceEngine engine) {
        super.addedToEngine(engine);
        cameras = engine.getEntitiesFor(Families.camera);
        cameraSystem = engine.cameraSystem;
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
        pointerPos.set(screenX, screenY);
        pointerDown = true;
        pointerClicked = true;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        pointerPos.set(screenX, screenY);
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
        pointerPos.set(screenX, screenY);
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}