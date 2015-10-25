package de.project.ice.ecs;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import de.project.ice.IceGame;
import de.project.ice.ecs.components.NameComponent;
import de.project.ice.ecs.systems.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IceEngine extends PooledEngine
{
    @NotNull
    public final StateSystem stateSystem;
    @NotNull
    public final AnimationSystem animationSystem;
    @NotNull
    public final RenderingSystem renderingSystem;
    @NotNull
    public final CameraSystem cameraSystem;
    @NotNull
    public final ScriptingSystem scriptingSystem;
    @NotNull
    public final MovementSystem movementSystem;
    @NotNull
    public final ControlSystem controlSystem;
    @NotNull
    public final BreathSystem breathSystem;
    @NotNull
    public final SoundSystem soundSystem;
    @NotNull
    public final IceGame game;


    @NotNull
    private final ImmutableArray<Entity> namedEntities;

    public IceEngine(@NotNull IceGame game)
    {
        super();
        this.game = game;
        stateSystem = new StateSystem();
        animationSystem = new AnimationSystem();
        cameraSystem = new CameraSystem();
        renderingSystem = new RenderingSystem();
        scriptingSystem = new ScriptingSystem();
        movementSystem = new MovementSystem();
        controlSystem = new ControlSystem();
        breathSystem = new BreathSystem();
        soundSystem = new SoundSystem();

        addSystem(stateSystem);
        addSystem(animationSystem);
        addSystem(cameraSystem);
        addSystem(renderingSystem);
        addSystem(scriptingSystem);
        addSystem(movementSystem);
        addSystem(controlSystem);
        addSystem(breathSystem);
        addSystem(soundSystem);

        namedEntities = getEntitiesFor(Family.all(NameComponent.class).get());
    }

    @Nullable
    public Entity getEntityByName(@NotNull String name)
    {
        for (Entity entity : namedEntities)
        {
            if (name.equals(Components.name.get(entity).name))
            {
                return entity;
            }
        }

        return null;
    }
}
