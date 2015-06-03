package de.project.ice.ecs.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import de.project.ice.ecs.Components;
import de.project.ice.ecs.Families;
import de.project.ice.ecs.components.*;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public class RenderingSystem extends SortedIteratingSystem {

    /**
     * configure the horizon line from top to x percent coverage to bottom;
     * e.g. 0.5f equals a horizon line at the screen center
     */
    private static float scaleHorizontPercentage = 0.7f;

    private ImmutableArray<Entity> cameras;


    @NotNull
    private SpriteBatch batch;
    @NotNull
    private Array<Entity> renderQueue = new Array<Entity>();

    public RenderingSystem (@NotNull SpriteBatch batch) {
        super(Families.renderable, new Comparator<Entity>() {
            @Override
            public int compare(Entity entityA, Entity entityB) {
                return (int) Math.signum(Components.transform.get(entityB).pos.z -
                        Components.transform.get(entityA).pos.z);
            }
        });

        this.batch = batch;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        cameras = engine.getEntitiesFor(Families.camera);
    }

    @Override
    public void update (float deltaTime) {
        super.update(deltaTime);

        // create the camera
        OrthographicCamera cam;
        if(cameras.size() > 0)
        {
            CameraComponent cameraComponent = cameras.first().getComponent(CameraComponent.class);
            cam = cameraComponent.camera;

            if(cam == null)
                return;
        }
        else
            return;

        cam.update();
        batch.setProjectionMatrix(cam.combined);
        batch.begin();

        // for each single entity
        for (Entity entity : renderQueue) {

            TextureComponent tex = Components.texture.get(entity);

            if (tex.region == null) {
                continue;
            }

            TransformComponent t = Components.transform.get(entity);

            float scaleDistance = 1f;
            // only scale distance of the entity has a transformComponent
            if(t != null)
            {
                // y-distances-scaling
               scaleDistance = calcDistanceScaling(t);
            }


            float width = tex.region.getRegionWidth();
            float height = tex.region.getRegionHeight();
            float originX = width * 0.5f;
            float originY = height * 0.5f;

            // draw the sprite in accordance with all calculated data (above)
            batch.draw(tex.region,
                    t.pos.x - originX, t.pos.y - originY,
                    originX, originY,
                    width, height,
                    t.scale.x * CameraSystem.PIXELS_TO_METRES * scaleDistance, t.scale.y * CameraSystem.PIXELS_TO_METRES * scaleDistance,
                    MathUtils.radiansToDegrees * t.rotation);
        }

        batch.end();
        renderQueue.clear();
    }

    @Override
    public void processEntity (Entity entity, float deltaTime) {
        renderQueue.add(entity);
    }

    /**
     *
     * @param percentage between 0.1 and 1.0
     */
    public static void setScaleHorizonPercentage(float percentage)
    {
        if(percentage >= 0.1f && percentage <= 1f)
            scaleHorizontPercentage = percentage;
    }

    public static float getScaleHorizonPercentage()
    {
        return scaleHorizontPercentage;
    }

    // TODO fix horizon crossing scale error
    private float calcDistanceScaling(TransformComponent t)
    {
        float scalePart = ((CameraSystem.FRUSTUM_HEIGHT * scaleHorizontPercentage) - (t.pos.y));
        //Gdx.app.log("Scale", "" + scalePart);
        float scaleDistance = 1f / (CameraSystem.FRUSTUM_HEIGHT * scaleHorizontPercentage);

        return (scaleDistance *= scalePart);
    }
}
