package de.project.ice.ecs.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import de.project.ice.ecs.Components;
import de.project.ice.ecs.Families;
import de.project.ice.ecs.components.*;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public class RenderingSystem extends SortedIteratingIceSystem {
    /**
     * x-axis dimension from far left to far right
     */
    public static final float FRUSTUM_WIDTH = 16f;
    /**
     * y-axis dimension from bottom to top
     */
    public static final float FRUSTUM_HEIGHT = 9f;
    public  static final float PIXELS_TO_METRES = 1.0f / 128.0f;

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
        batch.setProjectionMatrix(active_camera.combined);
        batch.begin();
        super.update(deltaTime);
        batch.end();
        debugRenderer.setProjectionMatrix(active_camera.combined);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(Color.RED);
        super.update(deltaTime);
        debugRenderer.end();
        forceSort();
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
                    1, 1,
                    MathUtils.radiansToDegrees * t.rotation);

        } else if (debugRenderer.isDrawing()) {
            debugRenderer.rect(t.pos.x, t.pos.y, width, height);
            debugRenderer.line(t.pos.x + width/2 - 0.1f, t.pos.y + originY, t.pos.x + width/2 + 0.1f, t.pos.y + originY);
            debugRenderer.line(t.pos.x + originX, t.pos.y + height/2 - 0.1f, t.pos.x + originX, t.pos.y + height/2 + 0.1f);
        }
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
