package de.project.ice.scripting;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import de.project.ice.IceGame;
import de.project.ice.ecs.IceEngine;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Base class for scripts
 * Note that all scripts have to be inside the "de.project.ice.scripting.scripts" package
 * or Have to be added to IceGame.gwt.xml for reflection to work inside the Browser
 */
public abstract class Script
{
    @Nullable
    private IceEngine engine = null;
    @Nullable
    private IceGame game = null;

    /**
     * Called when the script is loaded
     */
    public void onLoad()
    {
    }

    /**
     * Call once every cycle after all onUpdateEntity
     *
     * @param delta the delta time in seconds
     */
    public void onUpdate(float delta)
    {
    }

    /**
     * Call every cycle for every Entity
     *
     * @param delta the delta time in seconds
     */
    public void onUpdateEntity(Entity entity, float delta)
    {
    }

    /**
     * Called approximately every second
     */
    public void onTick()
    {
    }

    @NotNull
    public final IceEngine Engine() throws IllegalStateException
    {
        if (engine == null)
        {
            throw new IllegalStateException("An unloaded script tried to access the Engine");
        }
        return engine;
    }

    @NotNull
    public final IceGame Game() throws IllegalStateException
    {
        if (game == null)
        {
            throw new IllegalStateException("An unloaded script tried to access the Game");
        }
        return game;
    }

    public static Script loadScript(@NotNull String scriptName, IceGame game)
    {
        try
        {
            Class<? extends Script> clazz = ClassReflection.forName("de.project.ice.scripting.scripts." + scriptName);
            Script script = ClassReflection.newInstance(clazz);
            script.engine = game.engine;
            script.game = game;
            script.onLoad();
            return script;
        }
        catch (ReflectionException e)
        {
            Gdx.app.log(Script.class.getSimpleName(), "Unable to load script: " + scriptName);
        }
        return null;
    }

    public void onUnload()
    {

    }
}
