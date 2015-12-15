package de.project.ice.ecs.components;

import com.badlogic.gdx.graphics.OrthographicCamera;
import org.jetbrains.annotations.NotNull;

/**
 * ONLY one camera component should exist
 */
public class CameraComponent implements IceComponent<CameraComponent>
{
    @NotNull
    public OrthographicCamera camera = new OrthographicCamera(16, 9);

    @Override
    public void reset()
    {
        camera = new OrthographicCamera();
        camera.viewportWidth = 16;
        camera.viewportHeight = 9;
    }

    @Override
    public void copyTo(CameraComponent copy)
    {
        copy.camera = new OrthographicCamera(camera.viewportWidth, camera.viewportHeight);
        copy.camera.position.set(camera.position);
    }
}