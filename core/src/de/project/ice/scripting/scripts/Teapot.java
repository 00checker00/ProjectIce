package de.project.ice.scripting.scripts;


import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;
import de.project.ice.ecs.Components;
import de.project.ice.ecs.components.*;
import de.project.ice.scripting.Script;
import de.project.ice.utils.Assets;

public class Teapot extends Script
{
    @Override
    public void onAttachedToEntity(Entity entity)
    {
        Entity flame = Engine().createEntity();

        TransformComponent transformComponent = Engine().createComponent(TransformComponent.class);
        transformComponent.scale.set(1f, 0.1f);
        transformComponent.pos.set(Components.transform.get(entity).pos).add(0.01f, 0.45f);
        flame.add(transformComponent);

        TextureComponent textureComponent = Engine().createComponent(TextureComponent.class);
        flame.add(textureComponent);

        AnimationComponent animationComponent = Engine().createComponent(AnimationComponent.class);
        animationComponent.animations.put(1, Assets.createAnimation("candle_fire", 0.13f, Animation.PlayMode.LOOP));
        animationComponent.animation = 1;
        flame.add(animationComponent);

        ScriptComponent scriptComponent = Engine().createComponent(ScriptComponent.class);
        scriptComponent.scriptName = "TeapotFlame";
        flame.add(scriptComponent);

        NameComponent nameComponent = Engine().createComponent(NameComponent.class);
        nameComponent.name = "teapot_" + entity.getId() + "_flame";
        flame.add(nameComponent);

        flame.add(new InvisibilityComponent());
        Engine().addEntity(flame);
    }

    @Override
    public void onAttachedEntityRemoved(Entity entity)
    {
        Entity flame = Engine().getEntityByName("teapot_" + entity.getId() + "_flame");
        if (flame != null)
        {
            Engine().removeEntity(flame);
        }
    }
}
