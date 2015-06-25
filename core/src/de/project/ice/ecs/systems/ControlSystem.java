package de.project.ice.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.project.ice.ecs.Components;
import de.project.ice.ecs.Families;
import de.project.ice.ecs.IceEngine;
import de.project.ice.ecs.components.*;
import de.project.ice.hotspot.HotspotManager;
import de.project.ice.inventory.Inventory;
import de.project.ice.pathlib.*;
import de.project.ice.screens.CursorScreen;

import static de.project.ice.config.Config.*;

public class ControlSystem extends IteratingIceSystem implements InputProcessor {
    private ImmutableArray<Entity> walkareas;
    private ImmutableArray<Entity> cameras;
    private ImmutableArray<Entity> hotspots;
    OrthographicCamera active_camera = null;
    PathArea walkarea = null;
    HotspotManager.Hotspot active_hotspot = null;
    Entity hotspot_entity = null;
    HotspotManager hotspotManager;

    private Vector2 pointerPos = new Vector2();
    private boolean pointerDown = false;
    private boolean pointerWasDown = false;
    private boolean mouseUp = false;
    private boolean mouseDown = false;

    private CameraSystem cameraSystem;

    private PathCalculator mouseCalculator = new PathCalculator(0f);
    private PathCalculator pathCalculator = new PathCalculator(0.01f);

    public CursorScreen.Cursor primaryCursor = CursorScreen.Cursor.None;
    public CursorScreen.Cursor secondaryCursor = CursorScreen.Cursor.None;
    public Inventory.Item active_item = null;
    private IceEngine engine;

    @SuppressWarnings("unchecked")
    public ControlSystem() {
        super(Families.controllable);
    }

    @Override
    public void processEntity (Entity entity, float deltaTime) {
        MovableComponent move = Components.movable.get(entity);
        ControlComponent control = Components.control.get(entity);
        TextureComponent texture = Components.texture.get(entity);
        TransformComponent transform = Components.transform.get(entity);

        if (mouseDown) {
            entity.remove(UseComponent.class);
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) || Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && active_item != null)
                    active_item = null;

                activateHotspot(entity);
                switch (getActiveCursor()) {
                    case Walk:
                    case Take:
                    case Look:
                    case Speak:

                        float width = PIXELS_TO_METRES * (texture.region.data != null ? texture.region.data.getRegionWidth() : 0);

                        Vector2 target;
                        if (hotspot_entity != null) {
                            HotspotComponent hotspotComponent = Components.hotspot.get(hotspot_entity);
                            target = Components.transform.get(hotspot_entity).pos.cpy()
                                    .add(hotspotComponent.origin).add(hotspotComponent.targetPos);
                        } else {
                            Vector3 mouse_target = active_camera.unproject(new Vector3(pointerPos.x, pointerPos.y, 0f)); // unprojects UI coordinates to camera coordinates
                            target = new Vector2(mouse_target.x, mouse_target.y);
                        }

                        walkarea.waypoints.clear();
                        PathNode startNode = new PathNode(transform.pos.cpy().add(width / 2, 0));
                        walkarea.waypoints.add(startNode);
                        PathNode endNode = new PathNode(target);
                        walkarea.waypoints.add(endNode);
                        PathGraph graph = pathCalculator.computeGraph(walkarea);

                        GraphPath<PathNode> path = new DefaultGraphPath<PathNode>();

                        IndexedAStarPathFinder<PathNode> astar = new IndexedAStarPathFinder<PathNode>(graph);
                        astar.searchNodePath(startNode, endNode, new PathHeuristic(), path);

                        move.targetPositions.clear();

                        for (PathNode node : path) {
                            move.targetPositions.add(node.getPos().cpy().sub(width / 2, 0));
                        }
                        break;
                }
            }
        }
    }

    private void activateHotspot(Entity entity) {
        if (active_hotspot != null) {
            UseComponent useComponent = engine.createComponent(UseComponent.class);
            useComponent.target = hotspot_entity;
            useComponent.cursor = getActiveCursor();
            useComponent.item = active_item;
            entity.add(useComponent);
        }
    }

    private CursorScreen.Cursor getActiveCursor() {
        return Gdx.input.isButtonPressed(Input.Buttons.LEFT) || secondaryCursor == CursorScreen.Cursor.None ? primaryCursor : secondaryCursor;
    }

    @Override
    public void update(float deltaTime) {
        // update the active camera
        if(cameras.size() > 0)
            active_camera = cameras.first().getComponent(CameraComponent.class).camera;

        if(walkareas.size() > 0)
            walkarea = Components.walkarea.get(walkareas.first()).getArea();

        if (active_camera == null)
            return;

        if (pointerDown && !pointerWasDown) {
            mouseDown = true;
        } else if (!pointerDown && pointerWasDown) {
            mouseUp = true;
        }

        super.update(deltaTime);
        mouseUp = false;
        mouseDown = false;
        pointerWasDown = pointerDown;
    }

    @Override
    public void addedToEngine(IceEngine engine) {
        super.addedToEngine(engine);
        this.engine = engine;
        cameras = engine.getEntitiesFor(Families.camera);
        cameraSystem = engine.cameraSystem;
        walkareas = engine.getEntitiesFor(Families.walkArea);
        hotspots = engine.getEntitiesFor(Families.hotspot);
        hotspotManager = engine.game.hotspotManager;
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
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        pointerPos.set(screenX, screenY);
        pointerDown = false;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        pointerPos.set(screenX, screenY);
        primaryCursor = CursorScreen.Cursor.None;
        secondaryCursor = CursorScreen.Cursor.None;
        active_hotspot = null;
        hotspot_entity = null;

        if (active_camera != null) {
            Vector3 coords = active_camera.unproject(new Vector3(screenX, screenY, 0f));

            if (walkarea != null) {
                if (mouseCalculator.IsInside(walkarea, new Vector2(coords.x, coords.y))) {
                    primaryCursor = CursorScreen.Cursor.Walk;
                }
            }

            for (Entity entity : hotspots) {
                TransformComponent transform = Components.transform.get(entity);
                HotspotComponent hotspot = Components.hotspot.get(entity);

                if (new Rectangle(transform.pos.x + hotspot.origin.x,
                        transform.pos.y + hotspot.origin.y,
                        hotspot.width,
                        hotspot.height).contains(coords.x, coords.y)) {
                    active_hotspot = hotspotManager.get(hotspot.script);
                    if (active_hotspot != null) {
                        hotspot_entity = entity;
                        primaryCursor = active_hotspot.getPrimaryCursor();
                        secondaryCursor = active_hotspot.getSecondaryCursor();
                    }
                    break;
                }
            }
        }
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}