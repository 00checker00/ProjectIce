package de.project.ice.hotspot;

import com.badlogic.ashley.core.Entity;
import de.project.ice.Storage;
import de.project.ice.screens.CursorScreen;
import org.jetbrains.annotations.NotNull;

public class Wood extends HotspotManager.Hotspot {
    @NotNull
    @Override
    public CursorScreen.Cursor getPrimaryCursor() {
        return CursorScreen.Cursor.Take;
    }

    @NotNull
    @Override
    public CursorScreen.Cursor getSecondaryCursor() {
        return CursorScreen.Cursor.Look;
    }

    @Override
    public void use(@NotNull CursorScreen.Cursor cursor) {
        switch (cursor) {
            case Take:
                Entity wood = Engine().getEntityByName("wood");
                if (wood != null) {
                    Engine().removeEntity(wood);
                    Game().inventory.addItem("Wood");
                }
                break;

            case Look:
                Game().showMessages(Game().strings.get("s3_wood_desc"));
                break;
        }
    }
}
