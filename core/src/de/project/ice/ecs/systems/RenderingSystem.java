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
import de.project.ice.config.Config;
import de.project.ice.ecs.Components;
import de.project.ice.ecs.Families;
import de.project.ice.ecs.IceEngine;
import de.project.ice.ecs.components.*;
import de.project.ice.utils.Assets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

import static de.project.ice.config.Config.PIXELS_TO_METRES;
import static de.project.ice.config.Config.RENDER_DEBUG;

public class RenderingSystem extends SortedIteratingIceSystem
{
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
    public OrthographicCamera active_camera = null;

    @Nullable
    public OrthographicCamera getActive_camera()
    {
        return active_camera;
    }

    @NotNull
    private SpriteBatch batch;
    private ShapeRenderer debugRenderer;

    public RenderingSystem()
    {
        super(Families.renderable, new RenderingComparator());
        debugRenderer = new ShapeRenderer();

        this.batch = new SpriteBatch();
    }

    @Override
    public void addedToEngine(Engine engine)
    {
        super.addedToEngine(engine);
        cameras = engine.getEntitiesFor(Families.camera);
    }

    @Override
    public void update(float deltaTime)
    {
        // update the active camera
        if (cameras.size() > 0)
        {
            active_camera = cameras.first().getComponent(CameraComponent.class).getCamera();
        }

        if (active_camera == null)
        {
            return;
        }

        active_camera.update();
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setProjectionMatrix(active_camera.combined);
        batch.begin();
        super.update(deltaTime);

        for (Entity hotspot : hotspots)
        {
            renderHotspot(hotspot);
        }

        batch.end();

        if (RENDER_DEBUG)
        {
            debugRenderer.setProjectionMatrix(active_camera.combined);
            debugRenderer.begin(ShapeRenderer.ShapeType.Line);
            debugRenderer.setColor(Color.RED);
            super.update(deltaTime);
            debugRenderer.setColor(Color.BLUE);
            for (Entity hotspot : hotspots)
            {
                renderHotspot(hotspot);
            }
//            if (walkareas.size() > 0) {
//                WalkAreaComponent component = Components.walkarea.get(walkareas.first());
//                PathArea area = component.getArea();
//
//                debugRenderer.setColor(Color.PURPLE);
//
//                if (area != null && area.shape != null) {
//
//                    for (int i = 1; i < area.shape.vertices.size; i++)
//                        debugRenderer.line(
//                                area.shape.vertices.get(i).x,
//                                area.shape.vertices.get(i).y,
//                                area.shape.vertices.get(i - 1).x,
//                                area.shape.vertices.get(i - 1).y);
//
//                    debugRenderer.line(
//                            area.shape.vertices.get(0).x,
//                            area.shape.vertices.get(0).y,
//                            area.shape.vertices.get(area.shape.vertices.size - 1).x,
//                            area.shape.vertices.get(area.shape.vertices.size - 1).y);
//
//                    for (Shape hole : area.holes) {
//                        for (int i = 1; i < hole.vertices.size; i++)
//                            debugRenderer.line(
//                                    hole.vertices.get(i).x,
//                                    hole.vertices.get(i).y,
//                                    hole.vertices.get(i - 1).x,
//                                    hole.vertices.get(i - 1).y);
//
//                        debugRenderer.line(
//                                hole.vertices.get(0).x,
//                                hole.vertices.get(0).y,
//                                hole.vertices.get(hole.vertices.size - 1).x,
//                                hole.vertices.get(hole.vertices.size - 1).y);
//                    }
//                }
//
//            }
            debugRenderer.setColor(Color.GRAY);
            debugRenderer.line(0, -1, 0, 1);
            debugRenderer.line(-1, 0, 1, 0);
            debugRenderer.end();
        }
        forceSort();
    }

    private void renderHotspot(Entity entity)
    {
        TransformComponent transform = Components.transform.get(entity);
        HotspotComponent hotspot = Components.hotspot.get(entity);

        Vector2 pos = transform.getPos().cpy().add(hotspot.getOrigin());
        Vector2 origin = pos.cpy().add(hotspot.getOrigin());
        Vector2 center = origin.cpy().add(hotspot.getWidth() / 2, hotspot.getHeight() / 2);

        if (batch.isDrawing() && Gdx.input.isKeyPressed(Config.HOTSPOT_KEY))
        {
            Assets.Holder.TextureRegion region = Assets.INSTANCE.findRegion("hotspot");
            if (region.isValid())
                batch.draw(region.getData(), center.x - 0.25f, center.y - 0.25f, 0.5f, 0.5f);
        }
        else if (debugRenderer.isDrawing())
        {
            debugRenderer.rect(origin.x, origin.y, hotspot.getWidth(), hotspot.getHeight());
            cross(origin);
            cross(pos.cpy().sub(hotspot.getOrigin()).add(hotspot.getTargetPos()).add(hotspot.getOrigin()));
        }
    }

    @Override
    public void processEntity(Entity entity, float deltaTime)
    {
        TextureComponent tex = Components.texture.get(entity);

        if (tex.getRegion().getData() == null)
        {
            return;
        }

        TransformComponent t = Components.transform.get(entity);

        float scaleX = t.getScale().x;
        float scaleY = t.getScale().y;

        if (Components.breath.has(entity))
        {
            BreathComponent breath = Components.breath.get(entity);
            scaleX += scaleX * breath.getCurScale().x;
            scaleY += scaleY * breath.getCurScale().y;
        }

        float width = tex.getRegion().getData().getRegionWidth() * PIXELS_TO_METRES * scaleX;
        float height = tex.getRegion().getData().getRegionHeight() * PIXELS_TO_METRES * scaleY;
        float originX = width * 0.5f;
        float originY = height * 0.5f;

        if (batch.isDrawing())
        {

            // draw the sprite in accordance with all calculated data (above)
            batch.draw(tex.getRegion().getData(),
                    t.getPos().x, t.getPos().y,
                    originX, originY,
                    width, height,
                    t.getFlipHorizontal() ? -1f : 1f, t.getFlipVertical() ? -1f : 1f,
                    MathUtils.radiansToDegrees * t.getRotation());

        }
        else if (debugRenderer.isDrawing())
        {
            debugRenderer.rect(t.getPos().x, t.getPos().y, width, height);
            cross(t.getPos().cpy().add(width / 2, height / 2).add(originX, originY));
        }
    }

    private void cross(Vector2 pos)
    {
        debugRenderer.line(pos.x - 0.1f, pos.y, pos.x + 0.1f, pos.y);
        debugRenderer.line(pos.x, pos.y - 0.1f, pos.x, pos.y + 0.1f);
    }

    @Override
    public void addedToEngine(IceEngine engine)
    {
        hotspots = engine.getEntitiesFor(Families.hotspot);
        walkareas = engine.getEntitiesFor(Families.walkArea);
        super.addedToEngine(engine);
    }

    public static class RenderingComparator implements Comparator<Entity>
    {
        @Override
        public int compare(Entity entityA, Entity entityB)
        {
            int z = (int) Math.signum(Components.transform.get(entityB).getZ() -
                    Components.transform.get(entityA).getZ());
            if (z == 0)
            {
                return (int) Math.signum(Components.transform.get(entityB).getPos().y -
                        Components.transform.get(entityA).getPos().y);
            }
            else
            {
                return z;
            }
        }
    }
}
