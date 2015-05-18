package de.project.ice.ecs;

import com.badlogic.ashley.core.Family;
import de.project.ice.ecs.components.*;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unchecked")
public abstract class Families {
    @NotNull
    public static final Family renderable = Family.all(TransformComponent.class, TextureComponent.class).exclude(InvisibilityComponent.class).get();
    @NotNull
    public static final Family animated = Family.all(TransformComponent.class, TextureComponent.class, AnimationComponent.class, StateComponent.class)
            .exclude(InvisibilityComponent.class).get();
    @NotNull
    public static final Family scripted = Family.all(ScriptComponent.class).exclude(DisabledComponent.class).get();
    @NotNull
    public static final Family playerCharacter = Family.all(TransformComponent.class, TextureComponent.class, AnimationComponent.class, StateComponent.class,
            BreathComponent.class, MovableComponent.class, ControlComponent.class).exclude(InvisibilityComponent.class).get();

    @NotNull
    public static final Family camera = Family.all(CameraComponent.class).exclude(DisabledComponent.class).get();
}
