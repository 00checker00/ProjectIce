package de.project.ice.scripting.scripts;


import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;
import de.project.ice.ecs.Components;
import de.project.ice.ecs.components.*;
import de.project.ice.ecs.systems.AnimationSystem;
import de.project.ice.scripting.Script;
import de.project.ice.utils.Assets;

public class Teapot extends Script
{
    private long counter = 0;
    Entity flame;

    @Override
    public void onLoad()
    {
        super.onLoad();
        flame = Engine().createEntity();

        TransformComponent transformComponent = Engine().createComponent(TransformComponent.class);
        transformComponent.scale.set(1f, 0.1f);
        flame.add(transformComponent);

        StateComponent stateComponent = Engine().createComponent(StateComponent.class);
        flame.add(stateComponent);

        TextureComponent textureComponent = Engine().createComponent(TextureComponent.class);
        flame.add(textureComponent);

        AnimationComponent animationComponent = Engine().createComponent(AnimationComponent.class);
        animationComponent.animations.put(AnimationSystem.ANIMATION_DEFAULT, Assets.createAnimation("candle_fire", 0.13f, Animation.PlayMode.LOOP));
        flame.add(animationComponent);

        ScriptComponent scriptComponent = Engine().createComponent(ScriptComponent.class);
        scriptComponent.scriptName = "TeapotFlame";
        flame.add(scriptComponent);

        flame.add(new InvisibilityComponent());
    }

    @Override
    public void onTick()
    {
        super.onTick();
        counter++;
    }

    @Override
    public void onUnload()
    {
        Engine().removeEntity(flame);
    }

    @Override
    public void onUpdateEntity(Entity entity, float delta)
    {
        if (counter == 0)
        {
            Components.transform.get(flame).pos.set(Components.transform.get(entity).pos).add(0.01f, 0.45f);
            Engine().addEntity(flame);
            counter++;
        }
    }
}
