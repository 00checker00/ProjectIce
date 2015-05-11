package de.project.ice.scripting.scripts;

import com.badlogic.gdx.Gdx;
import de.project.ice.scripting.ScriptManager;

public class Bomb extends ScriptManager.Script {
    private static final String TAG = "BombScript";
    private int tick_count = 10;

    @Override
    public void onLoad () {
        setGlobal(true);
        Gdx.app.log(TAG, "Script loaded");
    }

    @Override
    public void onTick () {
        Gdx.app.log(TAG, "Bomb countdown: " + tick_count);
        tick_count--;

        // Simulate player moving between scenes 1 and 2
        if (tick_count == 7) {
            getManager().loadScript(Scene02_Load.class);
        } else if (tick_count == 2) {
            getManager().loadScript(Scene01_Load.class);
        }

        if (tick_count == 0) {
            // Bomb exploded...
            Gdx.app.log(TAG, "Bomb exploded... you lost");
            unload();
        }
    }
}
