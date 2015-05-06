package de.project.ice.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;
import org.jetbrains.annotations.Nullable;

public class TextureComponent extends Component implements Pool.Poolable {
    @Nullable
    public TextureRegion region = null;

    @Override
    public void reset () {
        region = null;
    }
}
