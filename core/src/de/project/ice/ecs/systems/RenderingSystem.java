package de.project.ice.ecs.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import de.project.ice.ecs.Components;
import de.project.ice.ecs.Families;
import de.project.ice.ecs.components.*;
import de.project.ice.utils.FakePerspectiveCamera;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public class RenderingSystem extends SortedIteratingIceSystem {
    private ImmutableArray<Entity> cameras;
    FakePerspectiveCamera active_camera = null;


    @NotNull
    private SpriteBatch batch;

    public RenderingSystem () {
        super(Families.renderable, new Comparator<Entity>() {
            @Override
            public int compare(Entity entityA, Entity entityB) {
                return (int) Math.signum(Components.transform.get(entityB).z -
                        Components.transform.get(entityA).z);
            }
        });

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
    }

    @Override
    public void processEntity (Entity entity, float deltaTime) {
        TextureComponent tex = Components.texture.get(entity);

        if (tex.region == null) {
            return;
        }

        TransformComponent t = Components.transform.get(entity);

        float scaleDistance = 1f;
        // only scale distance of the entity has a transformComponent
        if(t != null)
        {
            // y-distances-scaling
            scaleDistance = active_camera.calcDistanceScaling(t.pos.y);
        }

        float width = tex.region.getRegionWidth();
        float height = tex.region.getRegionHeight();
        float originX = width * 0.5f;
        float originY = height * 0.5f;

        batch.draw(tex.region, 0, 0);

        // draw the sprite in accordance with all calculated data (above)
        batch.draw(tex.region,
                t.pos.x - originX, t.pos.y - originY,
                originX, originY,
                width, height,
                t.scale.x * FakePerspectiveCamera.PIXELS_TO_METRES * scaleDistance, t.scale.y * FakePerspectiveCamera.PIXELS_TO_METRES * scaleDistance,
                MathUtils.radiansToDegrees * t.rotation);
    }
}
