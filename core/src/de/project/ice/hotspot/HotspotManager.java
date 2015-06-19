package de.project.ice.hotspot;


import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import de.project.ice.IceGame;
import de.project.ice.ecs.IceEngine;
import de.project.ice.inventory.Inventory;
import de.project.ice.screens.CursorScreen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class HotspotManager {
    @NotNull
    public final HashMap<String, Hotspot> hotspots = new HashMap<String, Hotspot>();
    @NotNull
    private final IceGame game;

    public HotspotManager(@NotNull IceGame game) {
        this.game = game;
    }

    public void loadHotspot(String name) {
        if (hotspots.containsKey(name))
            return;
        try {
            Class<Hotspot> clazz = ClassReflection.forName("de.project.ice.hotspot." + name);
            Hotspot hotspot = ClassReflection.newInstance(clazz);
            hotspot.hotspotManager = this;
            hotspots.put(name, hotspot);
        } catch (ReflectionException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    public Hotspot get(String name) {
        if (!hotspots.containsKey(name)) {
            loadHotspot(name);
        }

        return hotspots.get(name);
    }

    public static abstract class Hotspot {
        private HotspotManager hotspotManager = null;

        protected HotspotManager Manager() {
            return hotspotManager;
        }

        protected IceGame Game() {
            return hotspotManager.game;
        }

        protected IceEngine Engine() {
            return hotspotManager.game.engine;
        }

        public boolean canUseWith(@NotNull Inventory.Item item) {
            return false;
        }

        @NotNull
        public CursorScreen.Cursor getPrimaryCursor() {
            return CursorScreen.Cursor.None;
        }
        @NotNull
        public CursorScreen.Cursor getSecondaryCursor() {
            return CursorScreen.Cursor.None;
        }

        public void useWith(@NotNull Inventory.Item item) {

        }

        public void use(@NotNull CursorScreen.Cursor cursor) {

        }
    }
}
