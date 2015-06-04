package de.project.ice.scripting.scripts;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import de.project.ice.IceGame;
import de.project.ice.ecs.IceEngine;
import de.project.ice.ecs.components.*;
import de.project.ice.ecs.systems.AnimationSystem;
import de.project.ice.utils.FakePerspectiveCamera;

public class Scene01_Load extends SceneLoad {

    private IceEngine engine;

    @Override
    public void loadScene () {
        Gdx.app.log("Scene01", "Hooray you've reached Scene 1");

        // Create the Scene

        // load engine
        engine = getManager().getEngine();

        // create ONE camera entity
        Entity cameraEntity = engine.createEntity();
        CameraComponent cameraComponent = engine.createComponent(CameraComponent.class);
        cameraComponent.camera = new FakePerspectiveCamera();
        cameraComponent.camera.position.set(FakePerspectiveCamera.FRUSTUM_WIDTH / 2,
                FakePerspectiveCamera.FRUSTUM_HEIGHT / 2, 0);
        cameraEntity.add(cameraComponent);
        engine.addEntity(cameraEntity);

        // create the character
        createCharacterEntity();

    }

    private void createDoNothingEntity () {
        IceEngine engine = getManager().getEngine();
        Entity doNothingEntity = engine.createEntity();
        ScriptComponent scriptComponent = engine.createComponent(ScriptComponent.class);
        scriptComponent.script = getManager().loadScript(Bomb.class);
        doNothingEntity.add(scriptComponent);
        engine.addEntity(doNothingEntity);
    }

    private void createCharacterEntity() {
        Entity characterEntity = engine.createEntity();

        TransformComponent transformComponent = engine.createComponent(TransformComponent.class);
        transformComponent.pos = new Vector2(0, 0);
        characterEntity.add(transformComponent);

        BreathComponent breathComponent = engine.createComponent(BreathComponent.class);
        characterEntity.add(breathComponent);

        TextureComponent textureComponent = engine.createComponent(TextureComponent.class);
        textureComponent.region = IceGame.textureAtlas.findRegion("eski_lauf");
        characterEntity.add(textureComponent);

        AnimationComponent animationComponent = engine.createComponent(AnimationComponent.class);
        animationComponent.animations.put(AnimationSystem.ANIMATION_WALK, new Animation(1f / 3f, IceGame.textureAtlas.findRegions("eski_lauf"), Animation.PlayMode.LOOP));
        animationComponent.animations.put(AnimationSystem.ANIMATION_NONE, new Animation(Float.MAX_VALUE, IceGame.textureAtlas.findRegion("eski_lauf")));
        animationComponent.animations.put(AnimationSystem.ANIMATION_IDLE, new Animation(Float.MAX_VALUE, IceGame.textureAtlas.findRegion("eksi_rotznase")));
        characterEntity.add(animationComponent);

        StateComponent stateComponent = engine.createComponent(StateComponent.class);
        stateComponent.setAnimation(AnimationSystem.ANIMATION_WALK);
        characterEntity.add(stateComponent);

        ControlComponent controlComponent = engine.createComponent(ControlComponent.class);
        characterEntity.add(controlComponent);

        MovableComponent movableComponent = engine.createComponent(MovableComponent.class);
        characterEntity.add(movableComponent);

        engine.addEntity(characterEntity);
    }
}
