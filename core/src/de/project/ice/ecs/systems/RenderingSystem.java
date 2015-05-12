package de.project.ice.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import de.project.ice.ecs.Components;
import de.project.ice.ecs.Families;
import de.project.ice.ecs.components.AnimationComponent;
import de.project.ice.ecs.components.TextureComponent;
import de.project.ice.ecs.components.TransformComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public class RenderingSystem extends SortedIteratingSystem {
    static final float FRUSTUM_WIDTH = 15;
    static final float FRUSTUM_HEIGHT = 15;
    static final float PIXELS_TO_METRES = 1.0f / 128.0f;
    float test = 0;

    @NotNull
    private SpriteBatch batch;
    @NotNull
    private Array<Entity> renderQueue = new Array<Entity>();
    @NotNull
    private OrthographicCamera cam;

    public RenderingSystem (@NotNull SpriteBatch batch) {
        super(Families.renderable, new Comparator<Entity>() {
            @Override
            public int compare (Entity entityA, Entity entityB) {
                return (int) Math.signum(Components.transform.get(entityB).pos.z -
                        Components.transform.get(entityA).pos.z);
            }
        });

        this.batch = batch;

        cam = new OrthographicCamera(FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
        cam.position.set(FRUSTUM_WIDTH / 2, FRUSTUM_HEIGHT / 2, 0);
    }

    @Override
    public void update (float deltaTime) {
        super.update(deltaTime);

        test += deltaTime;

        cam.update();
        batch.setProjectionMatrix(cam.combined);
        batch.begin();

        for (Entity entity : renderQueue) {

            TextureComponent tex = Components.texture.get(entity);

            if (tex.region == null) {
                continue;
            }

            TransformComponent t = Components.transform.get(entity);

            float width = tex.region.getRegionWidth();
            float height = tex.region.getRegionHeight();
            float originX = width * 0.5f;
            float originY = height * 0.5f;


            batch.draw(tex.region,
                    t.pos.x - originX, t.pos.y - originY,
                    originX, originY,
                    width, height,
                    t.scale.x * PIXELS_TO_METRES, t.scale.y * PIXELS_TO_METRES,
                    MathUtils.radiansToDegrees * t.rotation);

            /*
            AnimationComponent a = Components.animation.get(entity);

            if(a == null)
                continue;

            batch.draw(a.animations.get(0).getKeyFrame(deltaTime, true),
                    t.pos.x - originX, t.pos.y - originY,
                    originX, originY,
                    width, height,
                    t.scale.x * PIXELS_TO_METRES, t.scale.y * PIXELS_TO_METRES,
                    MathUtils.radiansToDegrees * t.rotation);
                    */
        }

        batch.end();
        renderQueue.clear();
    }

    @Override
    public void processEntity (Entity entity, float deltaTime) {
        renderQueue.add(entity);
    }

    public OrthographicCamera getCamera () {
        return cam;
    }
}
