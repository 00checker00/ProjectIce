package de.project.ice.inventory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Antoni on 29.06.2015.
 */
public class Schnur extends Inventory.Item
{
    @NotNull
    @Override
    public String getIcon()
    {
        return "schnur_angel";
    }

    @Override
    public void combine(@NotNull Inventory.Item other)
    {
        Combine.combine((Hook) other, this);
    }

    @Override
    public boolean canCombine(@NotNull Inventory.Item other)
    {
        return other instanceof Hook;
    }

    @Nullable
    @Override
    public String getDescription()
    {
        return "s2_schnur_desc";
    }
}
