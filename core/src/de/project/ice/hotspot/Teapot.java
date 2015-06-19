package de.project.ice.hotspot;


import com.badlogic.ashley.core.Entity;
import de.project.ice.screens.CursorScreen;
import org.jetbrains.annotations.NotNull;

public class Teapot extends HotspotManager.Hotspot {
    @NotNull
    @Override
    public CursorScreen.Cursor getPrimaryCursor() {
        return CursorScreen.Cursor.Take;
    }

    @Override
    public void use(@NotNull CursorScreen.Cursor cursor) {
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
