package de.project.ice.scripting.scripts;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import de.project.ice.IceGame;
import de.project.ice.ecs.Engine;
import de.project.ice.ecs.components.*;

import javax.xml.crypto.dsig.Transform;
import javax.xml.soap.Text;

public class Scene01_Load extends SceneLoad {

    @Override
    public void loadScene () {
        Gdx.app.log("Scene01", "Hooray you've reached Scene 1");
        // Create the Scene
        createCharacterTestEntity();

        createDoNothingEntity ();
    }

    private void createDoNothingEntity () {
        Engine engine = getManager().getEngine();
        Entity doNothingEntity = engine.createEntity();
        ScriptComponent scriptComponent = engine.createComponent(ScriptComponent.class);
        scriptComponent.script = getManager().loadScript(Bomb.class);
        doNothingEntity.add(scriptComponent);
        engine.addEntity(doNothingEntity);
    }


    private void createCharacterTestEntity()
    {
        Engine engine = getManager().getEngine();

        Entity characterEntity = engine.createEntity();

        TransformComponent transformComponent = engine.createComponent(TransformComponent.class);
        transformComponent.pos = new Vector3(2f, 2f, 0f);
        characterEntity.add(transformComponent);

        TextureComponent textureComponent = engine.createComponent(TextureComponent.class);
        characterEntity.add(textureComponent);

        AnimationComponent animationComponent = engine.createComponent(AnimationComponent.class);
        animationComponent.animations.put(1, new Animation(1f/3f, IceGame.textureAtlas.findRegions("eski_lauf"), Animation.PlayMode.LOOP_PINGPONG));
        characterEntity.add(animationComponent);

        StateComponent stateComponent = engine.createComponent(StateComponent.class);
        stateComponent.set(1);
        characterEntity.add(stateComponent);
        engine.addEntity(characterEntity);
    }
}
