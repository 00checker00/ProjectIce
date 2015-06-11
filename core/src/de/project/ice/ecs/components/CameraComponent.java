package de.project.ice.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Pool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * ONLY one camera component should exist
 */
public class CameraComponent extends Component implements Pool.Poolable {
    @NotNull
    public OrthographicCamera camera = new OrthographicCamera();

    @Override
    public void reset () {
        camera = new OrthographicCamera();
    }
}