package de.project.ice.scripting.scripts;

import de.project.ice.scripting.ScriptManager;

public abstract class SceneLoad extends ScriptManager.Script {
    @Override
    public void onLoad () {
        // Unload every script except this, to possibly clean up the last scene
        getManager().unloadAll(this);

        loadScene();
    }

    public abstract void loadScene ();
}
