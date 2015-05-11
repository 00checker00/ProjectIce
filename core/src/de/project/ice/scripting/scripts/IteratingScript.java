package de.project.ice.scripting.scripts;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import de.project.ice.scripting.ScriptManager;

/**
 * Simple script which handles entity addition/removal and calls
 * onUpdate (Entity entity, float delta) for each entity every cycle
 * <p/>
 * To use subclass and override onUpdate (Entity entity, float delta),
 * if you override onUpdate (float delta), be sure to call super
 */
public abstract class IteratingScript extends ScriptManager.Script {
    private Array<Entity> entities = new Array<Entity>();

    @Override
    public void onCreate (Entity entity) {
        super.onCreate(entity);
        entities.add(entity);
    }

    @Override
    public void onRemove (Entity entity) {
        super.onRemove(entity);
        entities.removeValue(entity, true);
    }

    @Override
    public void onUpdate (float delta) {
        for (Entity entity : entities) {
            onUpdate(entity, delta);
        }
    }

    /**
     * Called for each entity the scripts is attached to every cycle
     *
     * @param entity the current entity to be processed
     * @param delta  the delta time in seconds
     */
    public void onUpdate (Entity entity, float delta) {
    }

    @Override
    public void onTick () {
        super.onTick();
    }
}
