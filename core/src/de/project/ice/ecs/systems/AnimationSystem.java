package de.project.ice.ecs.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import de.project.ice.ecs.Components;
import de.project.ice.ecs.Families;
import de.project.ice.ecs.components.AnimationComponent;
import de.project.ice.ecs.components.StateComponent;
import de.project.ice.ecs.components.TextureComponent;

public class AnimationSystem extends IteratingSystem {

    public AnimationSystem () {
        super(Families.animated);

    }

    @Override
    public void processEntity (Entity entity, float deltaTime) {
        TextureComponent tex = Components.texture.get(entity);
        AnimationComponent anim = Components.animation.get(entity);
        StateComponent state = Components.state.get(entity);

        Animation animation = anim.animations.get(state.get());

        if (animation != null) {
            tex.region = animation.getKeyFrame(state.time);
            Gdx.app.log("Animation", "" + state.time);
        }

        state.time += deltaTime;
    }
}
