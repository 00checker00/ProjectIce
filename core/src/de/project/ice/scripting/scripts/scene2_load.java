package de.project.ice.scripting.scripts;

import de.project.ice.scripting.Script;

/**
 * Created by Antoni on 29.06.2015.
 */
public class scene2_load extends Script
{
    @Override
    public void onLoad()
    {
        Game().inventory.addItem("Hook");
        Game().inventory.addItem("Stick");
    }
}
