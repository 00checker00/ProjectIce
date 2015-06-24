package de.project.ice.ecs.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.project.ice.ecs.Components;
import de.project.ice.ecs.Families;
import de.project.ice.ecs.IceEngine;
import de.project.ice.ecs.components.HotspotComponent;
import de.project.ice.ecs.components.*;
import de.project.ice.pathlib.PathArea;
import static de.project.ice.config.Config.*;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public class RenderingSystem extends SortedIteratingIceSystem {
    private ImmutableArray<Entity> hotspots;
    private ImmutableArray<Entity> walkareas;
    /**
     * x-axis dimension from far left to far right
     */
    public static final float FRUSTUM_WIDTH = 16f;
    /**
     * y-axis dimension from bottom to top
     */
    public static final float FRUSTUM_HEIGHT = 9f;

    private ImmutableArray<Entity> cameras;
    OrthographicCamera active_camera = null;


    @NotNull
    private SpriteBatch batch;
    private ShapeRenderer debugRenderer;

    public RenderingSystem () {
        super(Families.renderable, new RenderingComparator());
        debugRenderer = new ShapeRenderer();

        this.batch = new SpriteBatch();
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        cameras = engine.getEntitiesFor(Families.camera);
    }

    @Override
    public void update (float deltaTime) {
        // update the active camera
        if(cameras.size() > 0)
            active_camera = cameras.first().getComponent(CameraComponent.class).camera;

        if (active_camera == null)
            return;

        active_camera.update();
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setProjectionMatrix(active_camera.combined);
        batch.begin();
        super.update(deltaTime);
        batch.end();
        debugRenderer.setProjectionMatrix(active_camera.combined);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(Color.RED);
        super.update(deltaTime);
        debugRenderer.setColor(Color.BLUE);
        for (Entity hotspot : hotspots)
            renderHotspot(hotspot);
        if (walkareas.size() > 0) {
            WalkAreaComponent component = Components.walkarea.get(walkareas.first());
            PathArea area = component.getArea();

            if (area != null && area.shape != null) {

                debugRenderer.setColor(Color.PURPLE);

                for (int i = 1; i < area.shape.vertices.size; i++)
                    debugRenderer.line(
                            area.shape.vertices.get(i).x,
                            area.shape.vertices.get(i).y,
                            area.shape.vertices.get(i - 1).x,
                            area.shape.vertices.get(i - 1).y);

                debugRenderer.line(
                        area.shape.vertices.get(0).x,
                        area.shape.vertices.get(0).y,
                        area.shape.vertices.get(area.shape.vertices.size - 1).x,
                        area.shape.vertices.get(area.shape.vertices.size - 1).y);
            }

        }
        debugRenderer.setColor(Color.GRAY);
        debugRenderer.line(0, -1, 0, 1);
        debugRenderer.line(-1, 0, 1, 0);
        debugRenderer.end();
        forceSort();
    }

    private void renderHotspot(Entity entity) {
        TransformComponent transform = Components.transform.get(entity);
        HotspotComponent hotspot = Components.hotspot.get(entity);

        Vector2 pos = transform.pos.cpy().add(hotspot.origin);

        debugRenderer.rect(pos.x, pos.y, hotspot.width, hotspot.height);
        cross(pos.cpy().add(hotspot.origin).add(hotspot.width/2, hotspot.height/2));
        cross(pos.cpy().add(hotspot.targetPos).add(hotspot.origin));
    }

    @Override
    public void processEntity (Entity entity, float deltaTime) {
        TextureComponent tex = Components.texture.get(entity);

        if (tex.region.data == null) {
            return;
        }

        TransformComponent t = Components.transform.get(entity);

        float scaleX = t.scale.x;
        float scaleY = t.scale.y;

        if (Components.breath.has(entity)) {
            BreathComponent breath = Components.breath.get(entity);
            scaleX += scaleX * breath.curScale.x;
            scaleY += scaleY * breath.curScale.y;
        }

        float width = tex.region.data.getRegionWidth() * PIXELS_TO_METRES * scaleX;
        float height = tex.region.data.getRegionHeight() * PIXELS_TO_METRES * scaleY;
        float originX = width * 0.5f;
        float originY = height * 0.5f;

        if(batch.isDrawing()) {

            // draw the sprite in accordance with all calculated data (above)
            batch.draw(tex.region.data,
                    t.pos.x, t.pos.y,
                    originX, originY,
                    width, height,
                    t.flipHorizontal?-1f:1f, t.flipVertical?-1f:1f,
                    MathUtils.radiansToDegrees * t.rotation);

        } else if (debugRenderer.isDrawing()) {
            debugRenderer.rect(t.pos.x, t.pos.y, width, height);
            cross(t.pos.cpy().add(width/2, height/2).add(originX, originY));
        }
    }

    private void cross(Vector2 pos) {
        debugRenderer.line(pos.x - 0.1f, pos.y,        pos.x + 0.1f, pos.y       );
        debugRenderer.line(pos.x,        pos.y - 0.1f, pos.x,        pos.y + 0.1f);
    }

    @Override
    public void addedToEngine(IceEngine engine) {
        hotspots = engine.getEntitiesFor(Families.hotspot);
        walkareas = engine.getEntitiesFor(Families.walkArea);
        super.addedToEngine(engine);
    }

    public static class RenderingComparator implements Comparator<Entity> {
        @Override
        public int compare(Entity entityA, Entity entityB) {
            int z = (int) Math.signum(Components.transform.get(entityB).z -
                    Components.transform.get(entityA).z);
            if (z == 0)
                return (int) Math.signum(Components.transform.get(entityB).pos.y -
                    Components.transform.get(entityA).pos.y);
            else return z;
        }
    }
}
