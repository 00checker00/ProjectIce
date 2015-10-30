package de.project.ice.ecs.systems;

import com.badlogic.ashley.core.Entity;
import de.project.ice.ecs.Components;
import de.project.ice.ecs.Families;
import de.project.ice.ecs.components.AnimationComponent;
import de.project.ice.ecs.components.TextureComponent;
import de.project.ice.ecs.components.WalkingComponent;
import de.project.ice.utils.Assets;

public class AnimationSystem extends IteratingIceSystem
{
    public AnimationSystem()
    {
        super(Families.animated);

    }

    @Override
    public void processEntity(Entity entity, float deltaTime)
    {
        TextureComponent tex = Components.texture.get(entity);
        AnimationComponent anim = Components.animation.get(entity);

        anim.time += deltaTime;

        int animationNumber = anim.getAnimation();
        if (Components.walking.has(entity))
        {
            WalkingComponent walkingComponent = Components.walking.get(entity);
            if (walkingComponent.isWalking)
            {
                animationNumber = walkingComponent.animation;
            }
        }

        Assets.AnimationHolder animation = anim.animations.get(animationNumber);
        if (animation != null && animation.data != null && anim.animation > 0)
        {
            try
            {
                tex.region.data = animation.data.getKeyFrame(anim.time);
                tex.region.name = animation.name;
            }
            catch (ArithmeticException ignore)
            {
            }
        }
    }
}
