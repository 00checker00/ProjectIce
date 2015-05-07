package de.project.ice.scripting.scripts;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import de.project.ice.ecs.Engine;
import de.project.ice.ecs.components.ScriptComponent;

public class Scene01_Load extends SceneLoad {

    @Override
    public void loadScene () {
        Gdx.app.log("Scene01", "Hooray you've reached Scene 1");
        // Create the Scene
        createDoNothingEntity();
        createDoNothingEntity();
        createDoNothingEntity();
    }

    private void createDoNothingEntity () {
        Engine engine = getManager().getEngine();
        Entity doNothingEntity = engine.createEntity();
        ScriptComponent scriptComponent = engine.createComponent(ScriptComponent.class);
        scriptComponent.script = getManager().loadScript(Bomb.class);
        doNothingEntity.add(scriptComponent);
        engine.addEntity(doNothingEntity);
    }
}
