package de.project.ice.ecs.components;

import de.project.ice.utils.Assets;
import org.jetbrains.annotations.NotNull;

public class TextureComponent implements IceComponent<TextureComponent>
{
    @NotNull
    public Assets.TextureRegionHolder region = new Assets.TextureRegionHolder();

    @Override
    public void reset()
    {
        region = new Assets.TextureRegionHolder();
    }

    @Override
    public void copyTo(TextureComponent copy)
    {
        copy.region = Assets.findRegion(region.name);
    }
}
