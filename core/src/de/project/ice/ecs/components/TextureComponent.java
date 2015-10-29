package de.project.ice.ecs.components;

import de.project.ice.utils.Assets;
import org.jetbrains.annotations.NotNull;

public class TextureComponent implements IceComponent
{
    @NotNull
    public Assets.TextureRegionHolder region = new Assets.TextureRegionHolder();

    @Override
    public void reset()
    {
        region = new Assets.TextureRegionHolder();
    }

}
