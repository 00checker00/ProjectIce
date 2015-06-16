package de.project.ice.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.project.ice.ecs.Components;
import de.project.ice.ecs.Families;
import de.project.ice.ecs.IceEngine;
import de.project.ice.ecs.components.*;
import de.project.ice.pathlib.*;
import de.project.ice.screens.CursorScreen;

import static de.project.ice.config.Config.*;

public class ControlSystem extends IteratingIceSystem implements InputProcessor {
    private ImmutableArray<Entity> walkareas;
    private ImmutableArray<Entity> cameras;
    OrthographicCamera active_camera = null;
    PathArea walkarea = null;

    private Vector2 pointerPos = new Vector2();
    private boolean pointerDown = false;
    private boolean pointerClicked = false;
    private CameraSystem cameraSystem;

    private PathCalculator mouseCalculator = new PathCalculator(0f);
    private PathCalculator pathCalculator = new PathCalculator(0.01f);

    public CursorScreen.Cursor primaryCursor = CursorScreen.Cursor.None;
    public CursorScreen.Cursor secondaryCursor = CursorScreen.Cursor.None;

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

        if(pointerDown) {
            if(pointerClicked) {
                pointerClicked = false;
                if (primaryCursor == CursorScreen.Cursor.Walk) {
                    if (active_camera != null) {
                        Vector3 target = active_camera.unproject(new Vector3(pointerPos.x, pointerPos.y, 0f)); // unprojects UI coordinates to camera coordinates
                        float width = PIXELS_TO_METRES * (texture.region.data != null ? texture.region.data.getRegionWidth() : 0);

                        target = new Vector3(target.x, target.y, 0f);

                        walkarea.waypoints.clear();
                        PathNode startNode = new PathNode(transform.pos.cpy().add(width/2, 0));
                        walkarea.waypoints.add(startNode);
                        PathNode endNode = new PathNode(new Vector2(target.x, target.y));
                        walkarea.waypoints.add(endNode);
                        PathGraph graph = pathCalculator.computeGraph(walkarea);

                        GraphPath<PathNode> path = new DefaultGraphPath<PathNode>();

                        IndexedAStarPathFinder<PathNode> astar = new IndexedAStarPathFinder<PathNode>(graph);
                        astar.searchNodePath(startNode, endNode, new PathHeuristic(), path);

                        move.targetPositions.clear();

                        for (PathNode node  : path) {
                            move.targetPositions.add(node.getPos().cpy().sub(width/2, 0));
                        }
                    }
                }
            }
        }
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

        super.update(deltaTime);
    }

    @Override
    public void addedToEngine(IceEngine engine) {
        super.addedToEngine(engine);
        cameras = engine.getEntitiesFor(Families.camera);
        cameraSystem = engine.cameraSystem;
        walkareas = engine.getEntitiesFor(Families.walkArea);
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
        primaryCursor = CursorScreen.Cursor.None;
        secondaryCursor = CursorScreen.Cursor.None;

        if (walkarea != null && active_camera != null) {

            Vector3 coords = active_camera.unproject(new Vector3(screenX, screenY, 0f));
            if (mouseCalculator.IsInside(walkarea, new Vector2(coords.x, coords.y))) {
                primaryCursor = CursorScreen.Cursor.Walk;
            }
        }
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}