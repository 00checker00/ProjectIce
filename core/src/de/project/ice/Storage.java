package de.project.ice;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public abstract class Storage
{
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
    public static synchronized Global getGlobal()
    {
        if (Storage.GLOBAL == null)
        {
            Storage.GLOBAL = new Global();
        }
        return Storage.GLOBAL;
    }

    @NotNull
    @SuppressWarnings("ConstantConditions")
    public static synchronized Savestate getSavestate()
    {
        if (Storage.SAVESTATE == null)
        {
            Storage.SAVESTATE = new Savestate();
        }
        return Storage.SAVESTATE;
    }

    private Storage(@NotNull Preferences prefs)
    {
        this.prefs = prefs;
    }

    public Preferences put(@NotNull String key, boolean val)
    {
        return prefs.putString(key, Boolean.toString(val));
    }

    public Preferences put(@NotNull String key, int val)
    {
        return prefs.putString(key, Integer.toString(val));
    }

    public Preferences put(@NotNull String key, long val)
    {
        return prefs.putString(key, Long.toString(val));
    }

    public Preferences put(@NotNull String key, float val)
    {
        return prefs.putString(key, Float.toString(val));
    }

    public Preferences put(@NotNull String key, String val)
    {
        return prefs.putString(key, val);
    }

    public boolean getBoolean(@NotNull String key)
    {
        try
        {
            return Boolean.parseBoolean(prefs.getString(key));
        }
        catch (Exception ignore)
        {
            return false;
        }
    }

    public int getInteger(@NotNull String key)
    {
        try
        {
            return Integer.parseInt(prefs.getString(key));
        }
        catch (Exception ignore)
        {
            return 0;
        }
    }

    public long getLong(@NotNull String key)
    {
        try
        {
            return Long.parseLong(prefs.getString(key));
        }
        catch (Exception ignore)
        {
            return 0L;
        }
    }

    public float getFloat(@NotNull String key)
    {
        try
        {
            return Float.parseFloat(prefs.getString(key));
        }
        catch (Exception ignore)
        {
            return 0f;
        }
    }

    public String getString(@NotNull String key)
    {
        return prefs.getString(key);
    }


    public boolean getBoolean(@NotNull String key, boolean defaultValue)
    {
        try
        {
            return Boolean.parseBoolean(prefs.getString(key));
        }
        catch (Exception ignore)
        {
            return defaultValue;
        }
    }

    public int getInteger(@NotNull String key, int defaultValue)
    {
        try
        {
            return Integer.parseInt(prefs.getString(key));
        }
        catch (Exception ignore)
        {
            return defaultValue;
        }
    }

    public long getLong(@NotNull String key, long defaultValue)
    {
        try
        {
            return Long.parseLong(prefs.getString(key));
        }
        catch (Exception ignore)
        {
            return defaultValue;
        }
    }

    public float getFloat(@NotNull String key, float defaultValue)
    {
        try
        {
            return Float.parseFloat(prefs.getString(key));
        }
        catch (Exception ignore)
        {
            return defaultValue;
        }
    }

    @NotNull
    public String getString(@NotNull String key, @NotNull String defValue)
    {
        return prefs.getString(key, defValue);
    }

    public void clear()
    {
        prefs.clear();
    }

    public void remove(@NotNull String key)
    {
        prefs.remove(key);
    }

    public static class Global extends Storage
    {
        private Global()
        {
            super(Gdx.app.getPreferences(PREFIX + "_global"));
        }

        public void save()
        {
            prefs.flush();
        }
    }

    public static class Savestate extends Storage
    {
        private Savestate()
        {
            super(Gdx.app.getPreferences(PREFIX + "_temp"));
            prefs.clear();
        }

        public void load(int slot)
        {
            this.prefs.clear();
            Preferences p = Gdx.app.getPreferences(PREFIX + "_slot" + slot);
            this.prefs.put(p.get());
        }

        public void save(int slot)
        {
            Preferences p = Gdx.app.getPreferences(PREFIX + "_slot" + slot);
            p.put(this.prefs.get());
            p.clear();
            p.flush();
        }
    }
}
