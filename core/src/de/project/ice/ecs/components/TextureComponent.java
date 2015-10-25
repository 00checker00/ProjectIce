package de.project.ice.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import de.project.ice.utils.Assets;
import org.jetbrains.annotations.NotNull;

public class TextureComponent extends Component implements Pool.Poolable
{
    @NotNull
    public Assets.TextureRegionHolder region = new Assets.TextureRegionHolder();

    @Override
    public void reset()
    {
        region = new Assets.TextureRegionHolder();
    }

}
