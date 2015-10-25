package de.project.ice.inventory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Antoni on 29.06.2015.
 */
public class Hook extends Inventory.Item
{
    @NotNull
    @Override
    public String getIcon()
    {
        return "haken_angel";
    }

    @Override
    public void combine(@NotNull Inventory.Item other)
    {
        Combine.combine(this, (Schnur) other);
    }

    @Override
    public boolean canCombine(@NotNull Inventory.Item other)
    {
        return other instanceof Schnur;
    }

    @Nullable
    @Override
    public String getDescription()
    {
        return "s2_hook_desc";
    }
}
