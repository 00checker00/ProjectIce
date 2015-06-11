package de.project.ice.ecs;

import com.badlogic.ashley.core.ComponentMapper;
import de.project.ice.ecs.components.*;

import javax.naming.ldap.Control;

// TODO breathComponent, controlComponent(steuerbarer Charakter), movableComponent
public abstract class Components {
    public static final ComponentMapper<AnimationComponent> animation = ComponentMapper.getFor(AnimationComponent.class);
    public static final ComponentMapper<CameraComponent> camera = ComponentMapper.getFor(CameraComponent.class);
    public static final ComponentMapper<StateComponent> state = ComponentMapper.getFor(StateComponent.class);
    public static final ComponentMapper<TextureComponent> texture = ComponentMapper.getFor(TextureComponent.class);
    public static final ComponentMapper<TransformComponent> transform = ComponentMapper.getFor(TransformComponent.class);
    public static final ComponentMapper<DisabledComponent> disabled = ComponentMapper.getFor(DisabledComponent.class);
    public static final ComponentMapper<ScriptComponent> script = ComponentMapper.getFor(ScriptComponent.class);
    public static final ComponentMapper<ControlComponent> control = ComponentMapper.getFor(ControlComponent.class);
    public static final ComponentMapper<MovableComponent> movable = ComponentMapper.getFor(MovableComponent.class);
    public static final ComponentMapper<BreathComponent> breath = ComponentMapper.getFor(BreathComponent.class);
    public static final ComponentMapper<NameComponent> name = ComponentMapper.getFor(NameComponent.class);
}
