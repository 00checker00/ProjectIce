package de.project.ice.inventory;


import org.jetbrains.annotations.NotNull;

public class Teapot extends Inventory.Item {
    @NotNull
    @Override
    public String getIcon() {
        return "teekanne";
    }
}
