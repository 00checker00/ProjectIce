package de.project.ice.scripting;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import de.project.ice.ecs.IceEngine;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ScriptManager {
    @NotNull
    private HashMap<Class<? extends Script>, Script> loadedScripts = new HashMap<Class<? extends Script>, Script>();

    @NotNull
    private final IceEngine engine;

    public ScriptManager (@NotNull IceEngine engine) {
        this.engine = engine;
    }

    @NotNull
    public IceEngine getEngine () {
        return engine;
    }

    public Script loadScript (@NotNull Class<? extends Script> scriptClass) {
        if (loadedScripts.containsKey(scriptClass)) {
            return loadedScripts.get(scriptClass);
        }
        try {
            Script script = ClassReflection.newInstance(scriptClass);
            script.manager = this;
            script.onLoad();
            loadedScripts.put(scriptClass, script);
            return script;
        } catch (ReflectionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void unloadScript (@NotNull Script script) {
        Iterator<Script> it = loadedScripts.values().iterator();
        while (it.hasNext()) {
            if (it.next() == script) {
                it.remove();
                script.onUnload();
                script.manager = null;
                return;
            }
        }
    }

    public void unloadScript (@NotNull Class<? extends Script> scriptClass) {
        if (loadedScripts.containsKey(scriptClass)) {
            Script script = loadedScripts.get(scriptClass);
            loadedScripts.remove(scriptClass);
            script.onUnload();
            script.manager = null;
        }
    }

    /**
     * Unloads all currently loaded script
     *
     * @param forceUnload if true will even unload global scripts
     */
    public void unloadAll (boolean forceUnload) {
        Array<Script> scriptsToUnload = new Array<Script>();
        for (Script script : loadedScripts.values()) {
            if (!script.global || forceUnload)
                scriptsToUnload.add(script);
        }
        loadedScripts.clear();
        for (Script script : scriptsToUnload) {
            if (script.manager == this) {
                script.onUnload();
                script.manager = null;
            }
        }
    }

    /**
     * Unloads all currently loaded script
     * Global scripts will stay loaded
     */
    public void unloadAll () {
        unloadAll(false);
    }

    /**
     * Unloads all currently loaded script
     *
     * @param forceUnload if true will even unload global scripts
     * @param exceptions  scripts which shouldn't be unloaded
     */
    public void unloadAll (boolean forceUnload, Script... exceptions) {
        Array<Script> scriptsToUnload = new Array<Script>();
        List<Script> exceptionList = Arrays.asList(exceptions);
        Iterator<Script> it = loadedScripts.values().iterator();
        while (it.hasNext()) {
            Script script = it.next();
            if (exceptionList.contains(script) || (script.global && !forceUnload)) {
                continue;
            }
            scriptsToUnload.add(script);
            it.remove();
        }
        for (Script script : scriptsToUnload) {
            if (script.manager == this) {
                script.onUnload();
                script.manager = null;
            }
        }
    }

    /**
     * Unloads all currently loaded script
     * Global scripts will stay loaded
     *
     * @param exceptions scripts which shouldn't be unloaded
     */
    public void unloadAll (Script... exceptions) {
        unloadAll(false, exceptions);
    }

    /**
     * Base class for scripts
     * Note that all scripts have to be inside the "de.project.ice.scripting.scripts" package
     * or Have to be added to IceGame.gwt.xml for reflection to work inside the Browser
     */
    public static abstract class Script {
        @Nullable
        private ScriptManager manager = null;
        private boolean global = false;

        /**
         * Called when the script is loaded
         */
        public void onLoad () {
        }

        /**
         * Called when the script is unloaded
         */
        public void onUnload () {
        }

        /**
         * Called when the entity is created
         */
        public void onCreate (Entity entity) {
        }

        /**
         * Called when the entity is removed
         */
        public void onRemove (Entity entity) {
        }

        /**
         * Call every cycle
         *
         * @param delta the delta time in seconds
         */
        public void onUpdate (float delta) {
        }

        /**
         * Called approximately every second
         */
        public void onTick () {
        }

        /**
         * Unload the script
         */
        public final void unload () {
            if (manager != null) {
                manager.unloadScript(this);
            }
        }

        /**
         * @return the script manager by which this script is currently loaded
         * @throws IllegalStateException when script is not loaded
         */
        @NotNull
        public final ScriptManager getManager () throws IllegalStateException {
            if (manager == null) {
                throw new IllegalStateException("An unloaded script tried to access the ScriptManager");
            }
            return manager;
        }

        /**
         * Return true if script is currently loaded by a ScriptManager
         *
         * @return true or false
         */
        public final boolean isLoaded () {
            return manager != null;
        }

        /**
         * A global script doesn't getAnimation unloaded except when directly unloaded
         *
         * @return true or false
         */
        public boolean isGlobal () {
            return global;
        }

        /**
         * A global script doesn't getAnimation unloaded except when directly unloaded
         */
        public void setGlobal (boolean global) {
            this.global = global;
        }
    }
}
