package de.project.ice;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public abstract class Storage {
    @NotNull
    private static final String PREFIX = "de.project.ice";
    @Nullable
    private static Global GLOBAL;
    @Nullable
    private static Savestate SAVESTATE;

    @NotNull
    Preferences prefs;

    @NotNull
    @SuppressWarnings("ConstantConditions")
    public static synchronized Global getGlobal () {
        if (Storage.GLOBAL == null) {
            Storage.GLOBAL = new Global();
        }
        return Storage.GLOBAL;
    }

    @NotNull
    @SuppressWarnings("ConstantConditions")
    public static synchronized Savestate getSavestate () {
        if (Storage.SAVESTATE == null) {
            Storage.SAVESTATE = new Savestate();
        }
        return Storage.SAVESTATE;
    }

    private Storage(@NotNull Preferences prefs) {
        this.prefs = prefs;
    }

    public Preferences putBoolean(@NotNull String key, boolean val) {
        return prefs.putBoolean(key, val);
    }

    public Preferences putInteger(@NotNull String key, int val) {
        return prefs.putInteger(key, val);
    }

    public Preferences putLong(@NotNull String key, long val) {
        return prefs.putLong(key, val);
    }

    public Preferences putFloat(@NotNull String key, float val) {
        return prefs.putFloat(key, val);
    }

    public Preferences putString(@NotNull String key, String val) {
        return prefs.putString(key, val);
    }

    public boolean getBoolean(@NotNull String key) {
        return prefs.getBoolean(key);
    }

    public int getInteger(@NotNull String key) {
        return prefs.getInteger(key);
    }

    public long getLong(@NotNull String key) {
        return prefs.getLong(key);
    }

    public float getFloat(@NotNull String key) {
        return prefs.getFloat(key);
    }

    public String getString(@NotNull String key) {
        return prefs.getString(key);
    }

    public boolean getBoolean(@NotNull String key, boolean defValue) {
        return prefs.getBoolean(key, defValue);
    }

    public int getInteger(@NotNull String key, int defValue) {
        return prefs.getInteger(key, defValue);
    }

    public long getLong(@NotNull String key, long defValue) {
        return prefs.getLong(key, defValue);
    }

    public float getFloat(@NotNull String key, float defValue) {
        return prefs.getFloat(key, defValue);
    }

    @NotNull
    public String getString(@NotNull String key, @NotNull String defValue) {
        return prefs.getString(key, defValue);
    }

    public void clear() {
        prefs.clear();
    }

    public void remove(@NotNull String key) {
        prefs.remove(key);
    }

    public static class Global extends Storage {
        private Global() {
            super(Gdx.app.getPreferences(PREFIX + "_global"));
        }

        public void save() {
            prefs.flush();
        }
    }

    public static class Savestate extends Storage {
        private Savestate() {
            super(Gdx.app.getPreferences(PREFIX + "_temp"));
            prefs.clear();
        }

        public void load(int slot) {
            this.prefs.clear();
            Preferences p = Gdx.app.getPreferences(PREFIX + "_slot" + slot);
            this.prefs.put(p.get());
        }

        public void save(int slot) {
            Preferences p = Gdx.app.getPreferences(PREFIX + "_slot" + slot);
            p.put(this.prefs.get());
            p.clear();
            p.flush();
        }
    }
}
