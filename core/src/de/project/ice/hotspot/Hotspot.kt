package de.project.ice.hotspot

import com.badlogic.gdx.utils.ObjectMap
import de.project.ice.IceGame

import de.project.ice.screens.CursorScreen.Cursor

class Hotspot internal constructor(val id: String, val scriptObject: Any? = null) {
    val scriptUse = scriptObject as? Use
    val scriptUseWith = scriptObject as? UseWith

    fun canUseWith(item: String): Boolean {
        return scriptUseWith?.useableItems?.contains(item) ?: false
    }

    fun useWith(game: IceGame, item: String) {
        if (canUseWith(item))
            scriptUseWith?.useWith(game, item)
    }

    fun use(game: IceGame, cursor: Cursor) {
        scriptUse?.use(game, cursor)
    }

    companion object {
        val hotspots = ObjectMap<String, Hotspot>()

        operator fun get(id: String): Hotspot? {
            var hotspot: Hotspot? = hotspots.get(id, null)
            if (hotspot == null) {
                val classname = "de.project.ice.hotspot.hotspots.$id"
                try {
                    val clazz = Class.forName(classname)
                    val obj = clazz.newInstance()
                    hotspot = Hotspot(id, obj)
                } catch (ex: Exception) {
                    println("Error while loading hotspot with id: $id, cause by: ")
                    ex.printStackTrace()
                    hotspot = Hotspot(id)
                }
                hotspots.put(id, hotspot)
            }
            return hotspot
        }
    }
}
