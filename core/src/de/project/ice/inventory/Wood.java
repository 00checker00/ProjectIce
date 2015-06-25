package de.project.ice.inventory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Wood extends Inventory.Item {

    @NotNull
    @Override
    public String getIcon() {
        return "holz";
    }

    @Nullable
    @Override
    public String getDescription() {
        return "s3_wood_desc";
    }
}
