package de.project.ice.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;
import org.jetbrains.annotations.NotNull;

public class TransformComponent extends Component implements Pool.Poolable {
    @NotNull
    public final Vector3 pos = new Vector3();
    @NotNull
    public final Vector2 scale = new Vector2(1.0f, 1.0f);
    public float rotation = 0.0f;

    @Override
    public void reset () {
        pos.set(new Vector3());
        scale.set(1.0f, 1.0f);
        rotation = 0.0f;
    }
}
