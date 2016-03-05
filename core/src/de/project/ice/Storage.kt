package de.project.ice

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences


abstract class Storage internal constructor(internal var prefs: Preferences) {

    fun put(key: String, `val`: Boolean): Storage {
        prefs.putString(key, java.lang.Boolean.toString(`val`))
        return this
    }

    fun put(key: String, `val`: Int): Storage {
        prefs.putString(key, Integer.toString(`val`))
        return this
    }

    fun put(key: String, `val`: Long): Storage {
        prefs.putString(key, java.lang.Long.toString(`val`))
        return this
    }

    fun put(key: String, `val`: Float): Storage {
        prefs.putString(key, java.lang.Float.toString(`val`))
        return this
    }

    fun put(key: String, `val`: String): Storage {
        prefs.putString(key, `val`)
        return this
    }

    fun getBoolean(key: String): Boolean {
        try {
            return java.lang.Boolean.parseBoolean(prefs.getString(key))
        } catch (ignore: Exception) {
            return false
        }

    }

    fun getInteger(key: String): Int {
        try {
            return Integer.parseInt(prefs.getString(key))
        } catch (ignore: Exception) {
            return 0
        }

    }

    fun getLong(key: String): Long {
        try {
            return java.lang.Long.parseLong(prefs.getString(key))
        } catch (ignore: Exception) {
            return 0L
        }

    }

    fun getFloat(key: String): Float {
        try {
            return java.lang.Float.parseFloat(prefs.getString(key))
        } catch (ignore: Exception) {
            return 0f
        }

    }

    fun getString(key: String): String {
        return prefs.getString(key)
    }


    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        try {
            return java.lang.Boolean.parseBoolean(prefs.getString(key))
        } catch (ignore: Exception) {
            return defaultValue
        }

    }

    fun getInteger(key: String, defaultValue: Int): Int {
        try {
            return Integer.parseInt(prefs.getString(key))
        } catch (ignore: Exception) {
            return defaultValue
        }

    }

    fun getLong(key: String, defaultValue: Long): Long {
        try {
            return java.lang.Long.parseLong(prefs.getString(key))
        } catch (ignore: Exception) {
            return defaultValue
        }

    }

    fun getFloat(key: String, defaultValue: Float): Float {
        try {
            return java.lang.Float.parseFloat(prefs.getString(key))
        } catch (ignore: Exception) {
            return defaultValue
        }

    }

    fun getString(key: String, defValue: String): String {
        return prefs.getString(key, defValue)
    }

    fun clear() {
        prefs.clear()
    }

    fun remove(key: String) {
        prefs.remove(key)
    }

    class Global internal constructor() : Storage(Gdx.app.getPreferences(PREFIX + "_global")) {

        fun save() {
            prefs.flush()
        }
    }

    class Savestate internal constructor() : Storage(Gdx.app.getPreferences(PREFIX + "_temp")) {
        init {
            prefs.clear()
        }

        fun load(slot: Int) {
            this.prefs.clear()
            val p = Gdx.app.getPreferences(PREFIX + "_slot" + slot)
            this.prefs.put(p.get())
        }

        fun save(slot: Int) {
            val p = Gdx.app.getPreferences(PREFIX + "_slot" + slot)
            p.put(this.prefs.get())
            p.clear()
            p.flush()
        }
    }

    companion object {
        private val PREFIX = "de.project.ice"
        public val GLOBAL: Global by lazy { Global() }
        public val SAVESTATE: Savestate by lazy { Savestate() }
    }
}
