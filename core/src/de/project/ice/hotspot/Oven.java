package de.project.ice.hotspot;

import com.badlogic.ashley.core.Entity;
import de.project.ice.Storage;
import de.project.ice.ecs.components.InvisibilityComponent;
import de.project.ice.inventory.Inventory;
import de.project.ice.inventory.Wood;
import de.project.ice.screens.CursorScreen;
import org.jetbrains.annotations.NotNull;

public class Oven extends HotspotManager.Hotspot {
    @Override
    public boolean canUseWith(@NotNull Inventory.Item item) {
        return item instanceof Wood;
    }

    @NotNull
    @Override
    public CursorScreen.Cursor getPrimaryCursor() {
        return CursorScreen.Cursor.Look;
    }

    @Override
    public void useWith(@NotNull Inventory.Item item) {
        Game().inventory.removeItem("Wood");
        if (Engine().controlSystem.active_item == item)
            Engine().controlSystem.active_item = null;
        Entity fire = Engine().getEntityByName("oven_fire");
        if (fire != null)
            fire.remove(InvisibilityComponent.class);
        Storage.getSavestate().put("scene_03_oven_fire", true);
    }

    @Override
    public void use(@NotNull CursorScreen.Cursor cursor) {
        switch (cursor) {
            case Look:
                Game().showMessages(Game().strings.get("s3_oven_no_wood"));
                break;
        }
    }
}
