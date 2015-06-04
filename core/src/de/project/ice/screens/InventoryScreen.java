package de.project.ice.screens;

import de.project.ice.IceGame;
import org.jetbrains.annotations.NotNull;

public class InventoryScreen extends BaseScreenAdapter {
    public InventoryScreen(@NotNull IceGame game) {
        super(game);
    }

    @Override
    public int getPriority () {
        return 900;
    }
}
