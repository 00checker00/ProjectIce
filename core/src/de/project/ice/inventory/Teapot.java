package de.project.ice.inventory;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Teapot extends Inventory.Item {
    @NotNull
    @Override
    public String getIcon() {
        return "teekanne";
    }

    @Nullable
    @Override
    public String getDescription() {
        return "s3_teapot_desc";
    }
}
