package de.project.ice.pathtool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import de.project.ice.pathlib.PathNode;
import de.project.ice.pathlib.Shape;

import java.util.List;

/**
 * @author Aurelien Ribon | http://www.aurelienribon.com/
 */
public class CanvasDrawer {
    private static final Color SHAPE_COLOR = new Color(0.0f, 0.0f, 0.8f, 1);
    private static final Color SHAPE_LASTLINE_COLOR = new Color(0.5f, 0.5f, 0.5f, 1);
    private static final Color LINE_OF_SIGHT_COLOR = new Color(0.0f, 0.7f, 0.0f, 1);
    private static final Color PATH_COLOR = new Color(1.00f, 0.0f, 0.0f, 1);
    private static final Color MOUSESELECTION_FILL_COLOR = new Color(0.2f, 0.2f, 0.8f, 0.2f);
    private static final Color MOUSESELECTION_STROKE_COLOR = new Color(0.2f, 0.2f, 0.8f, 0.6f);
    private static final Color AXIS_COLOR = new Color(0.5f, 0.5f, 0.5f, 1);

    private final ShapeRenderer drawer = new ShapeRenderer();
    private final OrthographicCamera camera;

    public CanvasDrawer(OrthographicCamera camera) {
        this.camera = camera;
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    public void drawModel(ImageModel model, List<Vector2> selectedPoints, Vector2 nextPoint, Vector2 nearestPoint, boolean drawConnections, boolean drawArea, boolean drawPath, boolean creation) {
        if (model == null) return;
        drawer.setProjectionMatrix(camera.combined);
        if (drawConnections) drawConnections(model);
        if (drawArea) {
            for (Shape shape : model.shapes) drawShape(shape, nextPoint);
            for (Shape shape : model.shapes) drawPoints(shape, selectedPoints, nearestPoint);
        }
        if (drawPath) drawPathNodes(model);
        if (creation) drawNextPoint(nextPoint);
    }

    public void drawBoundingBox(Sprite sp) {
        if (sp == null) return;
        drawer.setProjectionMatrix(camera.combined);
        drawBoundingBox(sp.getWidth(), sp.getHeight());
    }

    public void drawMouseSelection(Vector2 p1, Vector2 p2) {
        if (p1 == null || p2 == null) return;
        drawer.setProjectionMatrix(camera.combined);
        drawMouseSelection(p1.x, p1.y, p2.x, p2.y);
    }

    // -------------------------------------------------------------------------
    // Internals
    // -------------------------------------------------------------------------

    private void drawBoundingBox(float w, float h) {
        Gdx.gl.glLineWidth(1);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        drawer.begin(ShapeRenderer.ShapeType.Line);
        drawer.setColor(AXIS_COLOR);
        drawer.rect(0, 0, w, h);
        drawer.end();
    }

    private void drawShape(Shape shape, Vector2 nextPoint) {
        Gdx.gl.glLineWidth(2);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        Array<Vector2> vs = shape.vertices;
        if (vs.size == 0) return;

        drawer.begin(ShapeRenderer.ShapeType.Line);
        drawer.setColor(SHAPE_COLOR);

        for (int i = 1; i < vs.size; i++) drawer.line(vs.get(i).x, vs.get(i).y, vs.get(i - 1).x, vs.get(i - 1).y);

        if (shape.closed) {
            drawer.setColor(SHAPE_COLOR);
            drawer.line(vs.get(0).x, vs.get(0).y, vs.get(vs.size - 1).x, vs.get(vs.size - 1).y);
        } else {
            drawer.setColor(SHAPE_LASTLINE_COLOR);
            drawer.line(vs.get(vs.size - 1).x, vs.get(vs.size - 1).y, nextPoint.x, nextPoint.y);
        }

        drawer.end();
    }

    private void drawPoints(Shape shape, List<Vector2> selectedPoints, Vector2 nearestPoint) {
        Gdx.gl.glLineWidth(2);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        float w = 10 * camera.zoom;

        for (Vector2 p : shape.vertices) {
            if (p == nearestPoint || (selectedPoints != null && selectedPoints.contains(p))) {
                drawer.begin(ShapeRenderer.ShapeType.Filled);
                drawer.setColor(SHAPE_COLOR);
                drawer.rect(p.cpy().sub(w / 2, w / 2).x, p.cpy().sub(w / 2, w / 2).y, w, w);
                drawer.end();
            } else {
                drawer.begin(ShapeRenderer.ShapeType.Line);
                drawer.setColor(SHAPE_COLOR);
                drawer.rect(p.cpy().sub(w / 2, w / 2).x, p.cpy().sub(w / 2, w / 2).y, w, w);
                drawer.end();
            }
        }
    }

    private void drawNextPoint(Vector2 nextPoint) {
        Gdx.gl.glLineWidth(2);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        float w = 10 * camera.zoom;

        if (nextPoint != null) {
            drawer.begin(ShapeRenderer.ShapeType.Filled);
            drawer.setColor(SHAPE_LASTLINE_COLOR);
            drawer.rect(nextPoint.cpy().sub(w / 2, w / 2).x, nextPoint.cpy().sub(w / 2, w / 2).y, w, w);
            drawer.end();
        }
    }

    private void drawConnections(ImageModel model) {
        Gdx.gl.glLineWidth(2);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        drawer.begin(ShapeRenderer.ShapeType.Line);
        drawer.setColor(LINE_OF_SIGHT_COLOR);

        Array<Connection<PathNode>> vs = model.linesOfSight;

        for (Connection<PathNode> connection : vs) {
            drawer.line(connection.getFromNode().getPos().x, connection.getFromNode().getPos().y,
                    connection.getToNode().getPos().x, connection.getToNode().getPos().y);
        }

        drawer.end();
    }

    private void drawPathNodes(ImageModel model) {
        Gdx.gl.glLineWidth(2);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        drawer.begin(ShapeRenderer.ShapeType.Line);
        drawer.setColor(PATH_COLOR);

        GraphPath<PathNode> vs = model.pathNodes;

        for (int i = 1; i < vs.getCount(); i++) {
            drawer.line(vs.get(i - 1).getPos().x, vs.get(i - 1).getPos().y,
                    vs.get(i).getPos().x, vs.get(i).getPos().y);
        }

        drawer.end();

        float w = 10 * camera.zoom;

        for (PathNode p : model.waypoints) {
            drawer.begin(ShapeRenderer.ShapeType.Filled);
            drawer.setColor(PATH_COLOR);
            drawer.rect(p.getPos().cpy().sub(w / 2, w / 2).x, p.getPos().cpy().sub(w / 2, w / 2).y, w, w);
            drawer.end();
        }
    }

    private void drawMouseSelection(float x1, float y1, float x2, float y2) {
        Gdx.gl.glLineWidth(3);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        Rectangle rect = new Rectangle(
                Math.min(x1, x2), Math.min(y1, y2),
                Math.abs(x2 - x1), Math.abs(y2 - y1)
        );

        drawer.begin(ShapeRenderer.ShapeType.Filled);
        drawer.setColor(MOUSESELECTION_FILL_COLOR);
        drawer.rect(rect.x, rect.y, rect.width, rect.height);
        drawer.end();

        drawer.begin(ShapeRenderer.ShapeType.Line);
        drawer.setColor(MOUSESELECTION_STROKE_COLOR);
        drawer.rect(rect.x, rect.y, rect.width, rect.height);
        drawer.end();
    }
}
