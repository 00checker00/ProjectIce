package de.project.ice.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.project.ice.IceGame;
import de.project.ice.ecs.components.WalkAreaComponent;
import de.project.ice.pathlib.PathArea;
import de.project.ice.pathlib.Shape;
import de.project.ice.screens.BaseScreenAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PathScreen extends BaseScreenAdapter {
    private static final float epsilon = 0.05f;

    @NotNull
    private PathArea pathArea;
    @NotNull
    private InputProcessor inputProcessor;
    @Nullable
    private Camera camera = null;
    @NotNull
    private ShapeRenderer shapeRenderer;
    @Nullable
    private Vector2 highlight = null;
    @Nullable
    private Vector2 selection = null;
    @Nullable
    private Vector2 mouseDown = null;


    public PathScreen (@NotNull final IceGame game) {
        super(game);

        pathArea = new PathArea();
        pathArea.shape = new Shape();

        shapeRenderer = new ShapeRenderer();

        inputProcessor = new InputAdapter() {
            @Override
            public boolean mouseMoved (int screenX, int screenY) {
                if (camera != null && game.isGamePaused()) {
                    Vector3 pos = camera.unproject(new Vector3(screenX, screenY, 0));

                    highlight = pointAtPos(new Vector2(pos.x, pos.y));
                }
                return false;
            }

            @Override
            public boolean touchDown (int screenX, int screenY, int pointer, int button) {
                selection = null;
                if (camera != null && game.isGamePaused()) {
                    Vector3 pos = camera.unproject(new Vector3(screenX, screenY, 0));

                    mouseDown = new Vector2(pos.x, pos.y);

                    Vector2 pointUnderMouse = pointAtPos(new Vector2(pos.x, pos.y));

                    if (button == Input.Buttons.LEFT) {
                        selection = pointUnderMouse;
                    } else if (button == Input.Buttons.RIGHT) {
                        if (pointUnderMouse == null) {
                            pathArea.shape.vertices.add(new Vector2(pos.x, pos.y));
                        } else {
                            pathArea.shape.vertices.removeValue(pointUnderMouse, true);
                        }
                    }

                    if (pointUnderMouse != null) {
                        return false;
                    }
                }
                return false;
            }

            @Override
            public boolean touchUp (int screenX, int screenY, int pointer, int button) {
                if (mouseDown != null) {
                    Vector3 pos = camera.unproject(new Vector3(screenX, screenY, 0));

                    if (pos.x >= mouseDown.x - epsilon && pos.x <= mouseDown.x + epsilon &&
                            pos.y >= mouseDown.y - epsilon && pos.y <= mouseDown.y + epsilon) {
                        if (pathArea.shape.vertices.size > 0 && selection == pathArea.shape.vertices.first() && pathArea.shape.vertices.size >= 3) {
                            pathArea.shape.closed = true;
                        }
                    }

                }
                mouseDown = null;
                return false;
            }

            @Override
            public boolean touchDragged (int screenX, int screenY, int pointer) {
                if (selection != null && game.isGamePaused()) {
                    Vector3 pos = camera.unproject(new Vector3(screenX, screenY, 0));
                    selection.set(pos.x, pos.y);
                    return true;
                }
                return false;
            }
        };

    }

    @Nullable
    private Vector2 pointAtPos (Vector2 pos) {
        for (Vector2 vector : pathArea.shape.vertices) {
            if (pos.x >= vector.x - epsilon && pos.x <= vector.x + epsilon &&
                    pos.y >= vector.y - epsilon && pos.y <= vector.y + epsilon) {
                return vector;
            }
        }
        return null;
    }

    @Override
    public int getPriority () {
        return 2;
    }

    @Override
    public void render () {
        if ((camera == null) || (pathArea.shape == null)) {
            return;
        }


        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (pathArea.shape.vertices.size > 0) {
            shapeRenderer.setProjectionMatrix(camera.combined);

            Vector2 previous = pathArea.shape.vertices.first();

            shapeRenderer.setColor(Color.CYAN);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            for (Vector2 vector : pathArea.shape.vertices) {
                shapeRenderer.line(previous.x, previous.y, vector.x, vector.y);

                previous = vector;
            }
            shapeRenderer.end();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            for (Vector2 vector : pathArea.shape.vertices) {
                shapeRenderer.circle(vector.x, vector.y, 0.05f, 10);
            }

            if (highlight != null) {
                Color colorBefore = shapeRenderer.getColor().cpy();
                shapeRenderer.setColor(Color.RED);
                shapeRenderer.circle(highlight.x, highlight.y, 0.05f, 10);
                shapeRenderer.setColor(colorBefore);
            }

            shapeRenderer.end();

            if (pathArea.shape.closed) {
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                Vector2 first = pathArea.shape.vertices.first();
                Vector2 last = pathArea.shape.vertices.get(pathArea.shape.vertices.size - 1);

                shapeRenderer.line(last.x, last.y, first.x, first.y);
                shapeRenderer.end();
            }
        }

    }

    @NotNull
    @Override
    public InputProcessor getInputProcessor () {
        return inputProcessor;
    }

    @Override
    public void update (float delta) {
        camera = game.engine.renderingSystem.getActive_camera();
        WalkAreaComponent walkAreaComponent = game.engine.controlSystem.getWalkArea();
        if (walkAreaComponent != null && walkAreaComponent.area != null) {
            pathArea = walkAreaComponent.area;
        }
    }

    @Override
    public void dispose () {
        super.dispose();
        shapeRenderer.dispose();
    }
}