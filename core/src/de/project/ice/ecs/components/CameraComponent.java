package de.project.ice.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import de.project.ice.utils.FakePerspectiveCamera;
import org.jetbrains.annotations.Nullable;

/**
 * ONLY one camera component should exist
 */
public class CameraComponent extends Component implements Pool.Poolable {
    @Nullable
    public FakePerspectiveCamera camera = null;

    @Override
    public void reset () {
        camera = null;
    }
}