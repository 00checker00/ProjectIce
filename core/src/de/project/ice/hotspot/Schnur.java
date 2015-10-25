package de.project.ice.hotspot;

import com.badlogic.ashley.core.Entity;
import de.project.ice.screens.CursorScreen;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Antoni on 29.06.2015.
 */
public class Schnur extends HotspotManager.Hotspot
{

    @NotNull
    @Override
    public CursorScreen.Cursor getPrimaryCursor()
    {
        return CursorScreen.Cursor.Take;
    }


    @Override
    public void use(@NotNull CursorScreen.Cursor cursor)
    {


        switch (cursor)
        {
            case Take:
                Entity schnur = Engine().getEntityByName("schnur");
                if (schnur != null)
                {
                    Engine().removeEntity(schnur);
                    Game().inventory.addItem("Schnur");
                }
                break;


        }
    }
}
