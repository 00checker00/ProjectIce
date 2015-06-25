package de.project.ice.hotspot;


import com.badlogic.ashley.core.Entity;
import de.project.ice.Storage;
import de.project.ice.screens.CursorScreen;
import org.jetbrains.annotations.NotNull;

public class Teapot extends HotspotManager.Hotspot {
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
        if (!Storage.getSavestate().getBoolean("scene_03_tea_ready")) {
            Game().showMessages(Game().strings.get("s3_tea_not_ready"));
            return;
        }

        switch (cursor) {
            case Take:
                Entity teapot = Engine().getEntityByName("teekanne");
                if (teapot != null) {
                    Engine().removeEntity(teapot);
                    Game().inventory.addItem("Teapot");
                }
                break;


        }
    }
}
