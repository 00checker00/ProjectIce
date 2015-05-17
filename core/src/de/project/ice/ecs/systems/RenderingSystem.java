package de.project.ice.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.SortedIteratingSystem;
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
import de.project.ice.ecs.components.TextureComponent;
import de.project.ice.ecs.components.TransformComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public class RenderingSystem extends SortedIteratingSystem {
    /**
     * x-axis dimension from far left to far right
     */
    static final float FRUSTUM_WIDTH = 16f;
    /**
     * y-axis dimension from bottom to top
     */
    static final float FRUSTUM_HEIGHT = 9f;
    static final float PIXELS_TO_METRES = 1.0f / 128.0f;

    /**
     * configure the horizon line from top to x percent coverage to bottom;
     * e.g. 1.5f equals a horizon line at the screen center
     */
    static final float SCALE_HORIZON_PERCENTAGE = 1.7f;

    static final float MOVEMENT_SPEED = 20f;

    private boolean isMousePressed = false;
    private boolean isMoving = false;

    private Vector3 mouseVector = new Vector3();
    private Vector2 mouseVectorUnprojected = new Vector2();
    private Vector2 directionVector = new Vector2();
    private Vector2 movementVector = new Vector2();
    private Vector2 velocityVector = new Vector2();


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
        //Gdx.app.log("DeltaTime", "" + deltaTime);

        cam.update();
        batch.setProjectionMatrix(cam.combined);
        batch.begin();

        // for each single entity with an animation
        for (Entity entity : renderQueue) {

            TextureComponent tex = Components.texture.get(entity);

            if (tex.region == null) {
                continue;
            }

            TransformComponent t = Components.transform.get(entity);

            // makes sprite movement to mouse coordinates possible
            handleMovement(t, deltaTime);

            // y-distances-scaling
            float scaleDistance = calcDistanceScaling(t);


            float width = tex.region.getRegionWidth();
            float height = tex.region.getRegionHeight();
            float originX = width * 0.5f;
            float originY = height * 0.5f;

            // draw the sprite in accordance with all calculated data (above)
            batch.draw(tex.region,
                    t.pos.x - originX, t.pos.y - originY,
                    originX, originY,
                    width, height,
                    t.scale.x * PIXELS_TO_METRES * scaleDistance, t.scale.y * PIXELS_TO_METRES * scaleDistance,
                    MathUtils.radiansToDegrees * t.rotation);
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

    // TODO handle unreachable destination (next possible waypoint in direction of path)
    private void handleMovement(TransformComponent t, float deltaTime)
    {
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if(!isMousePressed && !isMoving) {
                mouseVector.set(Gdx.input.getX(), Gdx.input.getY(), 0f); // the position of the mouse cursor
                cam.unproject(mouseVector); // unprojects UI coordinates to camera coordinates
                mouseVectorUnprojected.set(mouseVector.x, mouseVector.y); // set the unprojected coordinates to new vector

                isMoving = true;
                isMousePressed = true;
            }
        }
        else {
            isMousePressed = false;
        }

        if(isMoving) {
            directionVector.set(mouseVectorUnprojected).sub(t.pos.x, t.pos.y).nor(); // calculate direction vector
            velocityVector.set(directionVector).scl(MOVEMENT_SPEED); // calculate velocity with specified speed
            movementVector.set(velocityVector).scl(deltaTime); // calulates the movement vector for the CURRENT UPDATE!

            // if the destination has not been reached
            if(t.pos.dst2(mouseVectorUnprojected.x, mouseVectorUnprojected.y, 0f) > movementVector.len2())
                t.pos.add(movementVector.x, movementVector.y, 0f);
            else {
                t.pos.set(mouseVectorUnprojected.x, mouseVectorUnprojected.y, 0f);
                isMoving = false;
            }
        }
        else {
            isMoving = false;
        }
    }

    // TODO fix horizon crossing scale error
    private float calcDistanceScaling(TransformComponent t)
    {
        float scalePart = ((FRUSTUM_HEIGHT) - (t.pos.y * SCALE_HORIZON_PERCENTAGE));
        //Gdx.app.log("Scale", "" + scalePart);
        float scaleDistance = 0.9f / FRUSTUM_HEIGHT;
        if(scalePart >= scaleDistance)
            scaleDistance *= scalePart;

        return scaleDistance;
    }
}
