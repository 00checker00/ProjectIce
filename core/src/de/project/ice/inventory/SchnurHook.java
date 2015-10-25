package de.project.ice.inventory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Antoni on 29.06.2015.
 */
public class SchnurHook extends Inventory.Item
{
    @NotNull
    @Override
    public String getIcon()
    {
        return "hakenschnur_angel";
    }

    @Nullable
    @Override
    public String getDescription()
    {
        return "s2_schnurhook_desc";
    }
}
