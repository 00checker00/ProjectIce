package de.project.ice.ecs;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import de.project.ice.IceGame;
import de.project.ice.ecs.components.NameComponent;
import de.project.ice.ecs.systems.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IceEngine extends PooledEngine
{
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
    public final PathSystem pathSystem;
    @NotNull
    public final UseSystem useSystem;
    @NotNull
    public final IceGame game;


    @NotNull
    private final ImmutableArray<Entity> namedEntities;

    public IceEngine(@NotNull IceGame game)
    {
        super();
        this.game = game;
        animationSystem = new AnimationSystem();
        cameraSystem = new CameraSystem();
        renderingSystem = new RenderingSystem();
        scriptingSystem = new ScriptingSystem();
        movementSystem = new MovementSystem();
        controlSystem = new ControlSystem();
        breathSystem = new BreathSystem();
        soundSystem = new SoundSystem();
        pathSystem = new PathSystem();
        useSystem = new UseSystem();

        addSystem(animationSystem);
        addSystem(cameraSystem);
        addSystem(renderingSystem);
        addSystem(scriptingSystem);
        addSystem(movementSystem);
        addSystem(controlSystem);
        addSystem(breathSystem);
        addSystem(soundSystem);
        addSystem(pathSystem);
        addSystem(useSystem);

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

    public static String getEntityName(@NotNull Entity entity)
    {
        NameComponent nameComponent = Components.name.get(entity);
        if (nameComponent != null)
        {
            return nameComponent.name;
        }
        else
        {
            return "Unnamed(0x" + Long.toHexString(entity.getId()) + ")";
        }
    }

    @Override
    public void addEntity(Entity entity)
    {
        super.addEntity(entity);
        Gdx.app.log(getClass().getSimpleName(), "Added entity " + getEntityName(entity));
    }

    @Override
    public void removeEntity(Entity entity)
    {
        super.removeEntity(entity);
        Gdx.app.log(getClass().getSimpleName(), "Removed entity " + getEntityName(entity));
    }
}
