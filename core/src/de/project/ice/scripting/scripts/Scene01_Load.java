package de.project.ice.scripting.scripts;

import com.badlogic.gdx.Gdx;

public class Scene01_Load extends SceneLoad {
    public Scene01_Load() {
        super("Scene01");
    }

    @Override
    public void loadScene () {
        Gdx.app.log("Scene01", "Hooray you've reached Scene 1");

    }
}
