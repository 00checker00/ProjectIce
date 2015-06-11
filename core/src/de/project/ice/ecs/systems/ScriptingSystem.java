package de.project.ice.ecs.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import de.project.ice.ecs.Components;
import de.project.ice.ecs.Families;
import de.project.ice.ecs.components.ScriptComponent;
import de.project.ice.scripting.ScriptManager;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public class ScriptingSystem extends IntervalIceSystem implements EntityListener {
    HashSet<ScriptManager.Script> activeScripts = new HashSet<ScriptManager.Script>();
    @NotNull
    private Family family;
    @NotNull
    @SuppressWarnings("ConstantConditions")
    private ImmutableArray<Entity> entities = null;

    @SuppressWarnings("unchecked")
    public ScriptingSystem () {
        super(1f);
        this.family = Families.scripted;
    }

    @Override
    public void removedFromEngine (Engine engine) {
        super.removedFromEngine(engine);
        engine.removeEntityListener(this);
    }

    @Override
    public void addedToEngine (Engine engine) {
        super.addedToEngine(engine);
        entities = engine.getEntitiesFor(family);
        engine.addEntityListener(Families.scripted, this);
    }

    @Override
    public void update (float deltaTime) {
        activeScripts.clear();
        for (int i = 0; i < entities.size(); ++i) {
            ScriptComponent component = Components.script.get(entities.get(i));
            ScriptManager.Script script = component.script;
            if (script != null) {
                if (!script.isLoaded()) {
                    component.script = null;
                } else {
                    activeScripts.add(script);
                }
            }
        }
        for (ScriptManager.Script script : activeScripts) {
            script.onUpdate(deltaTime);
        }
        super.update(deltaTime);
    }

    @Override
    protected void updateInterval () {
        for (ScriptManager.Script script : activeScripts) {
            script.onTick();
        }
    }

    @Override
    public void entityAdded (Entity entity) {
        ScriptComponent component = Components.script.get(entity);
        if (component == null)
            return;

        ScriptManager.Script script = component.script;
        if (script == null)
            return;

        script.onCreate(entity);
    }

    @Override
    public void entityRemoved (Entity entity) {
        ScriptComponent component = Components.script.get(entity);
        if (component == null)
            return;

        ScriptManager.Script script = component.script;
        if (script == null)
            return;

        script.onRemove(entity);
    }
}
