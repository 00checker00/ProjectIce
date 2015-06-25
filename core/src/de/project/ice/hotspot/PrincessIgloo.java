package de.project.ice.hotspot;


import de.project.ice.Storage;
import de.project.ice.inventory.Inventory;
import de.project.ice.screens.CursorScreen;
import org.jetbrains.annotations.NotNull;

import static de.project.ice.config.Config.*;

public class PrincessIgloo extends HotspotManager.Hotspot {
    @NotNull
    @Override
    public CursorScreen.Cursor getPrimaryCursor() {
        return CursorScreen.Cursor.Speak;
    }

    @NotNull
    @Override
    public CursorScreen.Cursor getSecondaryCursor() {
        return CursorScreen.Cursor.Look;
    }

    @Override
    public void useWith(@NotNull Inventory.Item item) {
        Storage.getSavestate().put("PrincessGiveTea", true).flush();
        Game().showDialog("PrincessIgloo");
        Game().inventory.removeItem("Teapot");
        if (Engine().controlSystem.active_item == item)
            Engine().controlSystem.active_item = null;
    }

    @Override
    public boolean canUseWith(@NotNull Inventory.Item item) {
        return item instanceof de.project.ice.inventory.Teapot;
    }

    @Override
    public void use(@NotNull CursorScreen.Cursor cursor) {
        switch (cursor) {
            case Speak:
                Game().showDialog("PrincessIgloo");
                break;
            case Look:
                Game().showMessages("Prinzessin ________. Meine groﬂe Liebe.");
                break;
        }
    }
}
