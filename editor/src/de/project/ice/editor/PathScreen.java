package de.project.ice.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import de.project.ice.IceGame;
import de.project.ice.pathlib.PathArea;
import de.project.ice.pathlib.Shape;
import de.project.ice.screens.BaseScreenAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PathScreen extends BaseScreenAdapter
{
    private static final float epsilon = 0.05f;

    @NotNull
    private PathArea pathArea;
    @NotNull
    private InputProcessor inputProcessor;
    @Nullable
    private Camera camera = null;
    @NotNull
    private FilledPolygonShapeRenderer shapeRenderer;
    @Nullable
    private Vector2 highlight = null;
    @Nullable
    private Vector2 selection = null;
    @Nullable
    private Vector2 mouseDown = null;
    @Nullable
    private Vector2 insertPos = null;
    private int insertIndex = 0;


    public PathScreen(@NotNull final IceGame game)
    {
        super(game);

        pathArea = new PathArea();
        pathArea.shape = new Shape();

        shapeRenderer = new FilledPolygonShapeRenderer();

        inputProcessor = new InputAdapter()
        {
            @Override
            public boolean mouseMoved(int screenX, int screenY)
            {
                insertPos = null;
                if (camera != null && game.isGamePaused())
                {
                    Vector3 pos = camera.unproject(new Vector3(screenX, screenY, 0));

                    highlight = pointAtPos(new Vector2(pos.x, pos.y));

                    if (highlight == null)
                    {
                        for (int i = 0; i < pathArea.shape.vertices.size; ++i)
                        {
                            Vector2 cur = pathArea.shape.vertices.get(i);
                            Vector2 next = pathArea.shape.vertices.get((i + 1) % pathArea.shape.vertices.size);

                            Vector2 dir = next.cpy().sub(cur).nor();
                            Vector2 rot = dir.cpy().rotate90(0).scl(epsilon);

                            Array<Vector2> bounds = new Array<Vector2>(new Vector2[]{
                                    cur.cpy().add(rot),
                                    cur.cpy().sub(rot),
                                    next.cpy().sub(rot),
                                    next.cpy().add(rot),
                            });

                            if (Intersector.isPointInPolygon(bounds, new Vector2(pos.x, pos.y)))
                            {
                                insertPos = cur.cpy().add(dir.cpy().scl(new Vector2(pos.x, pos.y).sub(cur).len()));
                                insertIndex = i + 1;
                                break;
                            }
                        }
                    }
                }

                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button)
            {
                selection = null;
                if (camera != null && game.isGamePaused())
                {
                    Vector3 pos = camera.unproject(new Vector3(screenX, screenY, 0));

                    mouseDown = new Vector2(pos.x, pos.y);

                    Vector2 pointUnderMouse = pointAtPos(new Vector2(pos.x, pos.y));

                    if (button == Input.Buttons.LEFT)
                    {
                        selection = pointUnderMouse;
                    }
                    else if (button == Input.Buttons.RIGHT)
                    {
                        if (insertPos != null)
                        {
                            pathArea.shape.vertices.insert(insertIndex, insertPos);
                            insertPos = null;
                        }
                        else if (pointUnderMouse == null)
                        {
                            pathArea.shape.vertices.add(new Vector2(pos.x, pos.y));
                        }
                        else
                        {
                            pathArea.shape.vertices.removeValue(pointUnderMouse, true);
                            highlight = null;
                        }
                        pathArea.shape.closed = pathArea.shape.vertices.size >= 3;
                    }

                    if (pointUnderMouse != null)
                    {
                        return false;
                    }
                }
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer)
            {
                if (selection != null && game.isGamePaused())
                {
                    Vector3 pos = camera.unproject(new Vector3(screenX, screenY, 0));
                    selection.set(pos.x, pos.y);
                    return true;
                }
                return false;
            }
        };

    }

    @Nullable
    private Vector2 pointAtPos(Vector2 pos)
    {
        for (Vector2 vector : pathArea.shape.vertices)
        {
            if (pos.x >= vector.x - epsilon && pos.x <= vector.x + epsilon &&
                    pos.y >= vector.y - epsilon && pos.y <= vector.y + epsilon)
            {
                return vector;
            }
        }
        return null;
    }

    @Override
    public int getPriority()
    {
        return 2;
    }

    @Override
    public void render()
    {
        if ((camera == null))
        {
            return;
        }

        boolean blendEnabled = Gdx.gl.glIsEnabled(GL20.GL_BLEND);

        if (!blendEnabled)
        {
            Gdx.gl.glEnable(GL20.GL_BLEND);
        }

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (pathArea.shape.vertices.size > 0)
        {
            shapeRenderer.setProjectionMatrix(camera.combined);

            if (pathArea.shape.closed)
            {
                shapeRenderer.begin(FilledPolygonShapeRenderer.ShapeType.Filled);

                shapeRenderer.setColor(new Color(0, 0, 1f, 0.1f));

                float[] vertices = new float[pathArea.shape.vertices.size * 2];
                for (int i = 0; i < pathArea.shape.vertices.size; ++i)
                {
                    Vector2 vector = pathArea.shape.vertices.get(i);
                    vertices[2 * i] = vector.x;
                    vertices[2 * i + 1] = vector.y;
                }
                shapeRenderer.polygon(vertices);
                shapeRenderer.end();
            }

            Vector2 previous = pathArea.shape.vertices.first();

            shapeRenderer.setColor(Color.CYAN);
            shapeRenderer.begin(FilledPolygonShapeRenderer.ShapeType.Line);
            for (Vector2 vector : pathArea.shape.vertices)
            {
                shapeRenderer.line(previous.x, previous.y, vector.x, vector.y);

                previous = vector;
            }
            shapeRenderer.end();

            shapeRenderer.begin(FilledPolygonShapeRenderer.ShapeType.Filled);
            for (Vector2 vector : pathArea.shape.vertices)
            {
                shapeRenderer.circle(vector.x, vector.y, 0.05f, 10);
            }

            if (highlight != null)
            {
                Color colorBefore = shapeRenderer.getColor().cpy();
                shapeRenderer.setColor(Color.RED);
                shapeRenderer.circle(highlight.x, highlight.y, 0.05f, 10);
                shapeRenderer.setColor(colorBefore);
            }

            if (insertPos != null)
            {
                Color colorBefore = shapeRenderer.getColor().cpy();
                shapeRenderer.setColor(Color.GREEN);
                shapeRenderer.circle(insertPos.x, insertPos.y, 0.05f, 10);
                shapeRenderer.setColor(colorBefore);
            }

            shapeRenderer.end();

            if (pathArea.shape.closed)
            {
                shapeRenderer.begin(FilledPolygonShapeRenderer.ShapeType.Line);
                Vector2 first = pathArea.shape.vertices.first();
                Vector2 last = pathArea.shape.vertices.get(pathArea.shape.vertices.size - 1);

                shapeRenderer.line(last.x, last.y, first.x, first.y);
                shapeRenderer.end();
            }
        }

        if (!blendEnabled)
        {
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

    }

    @NotNull
    @Override
    public InputProcessor getInputProcessor()
    {
        return inputProcessor;
    }

    @Override
    public void update(float delta)
    {
        camera = game.engine.renderingSystem.getActive_camera();
        pathArea = game.engine.pathSystem.getWalkArea();
    }

    @Override
    public void dispose()
    {
        super.dispose();
        shapeRenderer.dispose();
    }
}