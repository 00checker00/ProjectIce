package de.project.ice.scripting.scripts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import de.project.ice.scripting.ScriptManager;
import de.project.ice.utils.SceneLoader;

import java.io.IOException;

public abstract class SceneLoad extends ScriptManager.Script {
    private final String scene;

    public SceneLoad(String scene) {
        this.scene = scene;
    }
    @Override
    public void onLoad () {
        // Unload every script except this, to possibly clean up the last scene
        getManager().unloadAll(this);

        try {
            FileHandle file = Gdx.files.internal("scenes/" + scene + ".scene");
            SceneLoader.loadScene(getManager().getEngine(), file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SceneLoader.LoadException e) {
            e.printStackTrace();
        }

        loadScene();
    }

    public abstract void loadScene ();
}
