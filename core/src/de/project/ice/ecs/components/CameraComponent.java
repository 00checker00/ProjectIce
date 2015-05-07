package de.project.ice.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Pool;
import org.jetbrains.annotations.Nullable;

public class CameraComponent extends Component implements Pool.Poolable {
    @Nullable
    public Entity target = null;
    @Nullable
    public OrthographicCamera camera = null;

    @Override
    public void reset () {
        target = null;
        camera = null;
    }
}