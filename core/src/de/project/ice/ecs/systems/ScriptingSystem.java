package de.project.ice.ecs.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import de.project.ice.ecs.Components;
import de.project.ice.ecs.Families;
import de.project.ice.ecs.IceEngine;
import de.project.ice.ecs.components.ScriptComponent;
import de.project.ice.scripting.Script;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public class ScriptingSystem extends IntervalIceSystem implements EntityListener
{
    HashSet<Script> activeScripts = new HashSet<Script>();
    @NotNull
    private Family family;
    @NotNull
    @SuppressWarnings("ConstantConditions")
    private ImmutableArray<Entity> entities = null;
    private IceEngine engine;

    @SuppressWarnings("unchecked")
    public ScriptingSystem()
    {
        super(1f);
        this.family = Families.scripted;
    }

    @Override
    public void removedFromEngine(Engine engine)
    {
        super.removedFromEngine(engine);
        engine.removeEntityListener(this);
    }

    @Override
    public void addedToEngine(IceEngine engine)
    {
        super.addedToEngine(engine);
        entities = engine.getEntitiesFor(family);
        this.engine = engine;
        engine.addEntityListener(family, this);
    }

    @Override
    public void update(float deltaTime)
    {
        activeScripts.clear();
        for (int i = 0; i < entities.size(); ++i)
        {
            Entity entity = entities.get(i);
            ScriptComponent component = Components.script.get(entity);
            Script script = component.script;
            if (script == null)
            {
                component.script = Script.loadScript(component.scriptName, engine.game);
                if (component.script != null)
                {
                    component.script.onLoad();
                }
            }
            if (script != null)
            {
                activeScripts.add(script);
                script.onUpdateEntity(entity, deltaTime);
            }
        }
        for (Script script : activeScripts)
        {
            script.onUpdate(deltaTime);
        }
        super.update(deltaTime);
    }

    @Override
    protected void updateInterval()
    {
        for (Script script : activeScripts)
        {
            script.onTick();
        }
    }

    @Override
    public void entityAdded(Entity entity)
    {

    }

    @Override
    public void entityRemoved(Entity entity)
    {
        if (Components.script.has(entity))
        {
            Script script = Components.script.get(entity).script;
            if (script != null)
            {
                script.onUnload();
            }
        }
    }
}
