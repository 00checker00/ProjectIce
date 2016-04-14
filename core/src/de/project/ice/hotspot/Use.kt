package de.project.ice.hotspot

import de.project.ice.IceGame
import de.project.ice.screens.CursorScreen

interface Use {
    fun use(game: IceGame, cursor: CursorScreen.Cursor, hotspotId: String)

    abstract class Adapter : Use {
        override fun use(game: IceGame, cursor: CursorScreen.Cursor, hotspotId: String) {
            when (cursor) {
                CursorScreen.Cursor.None -> {}
                CursorScreen.Cursor.Walk -> walk(game, hotspotId)
                CursorScreen.Cursor.Look -> look(game, hotspotId)
                CursorScreen.Cursor.Speak -> speak(game, hotspotId)
                CursorScreen.Cursor.Take -> take(game, hotspotId)
                CursorScreen.Cursor.Use -> use(game, hotspotId)
            }
        }

        protected open fun walk(game: IceGame, hotspotId: String) {
        }

        protected open fun look(game: IceGame, hotspotId: String) {
        }

        protected open fun speak(game: IceGame, hotspotId: String) {
        }

        protected open fun take(game: IceGame, hotspotId: String) {
        }

        protected open fun use(game: IceGame, hotspotId: String){

        }

    }

    open class Take(private val entityName: String, private val itemName: String) : Adapter() {

        override fun take(game: IceGame, hotspotId: String) {
            val entity = game.engine.getEntityByName(entityName)
            if (entity != null) {
                game.engine.removeEntity(entity)
                game.inventory.addItem(itemName)
            } else {
                throw RuntimeException("Trying to take non-existing entity: " + entityName)
            }
        }
    }

    abstract class With : Use {
        override fun use(game: IceGame, cursor: CursorScreen.Cursor, hotspotId: String) {
            use(game, hotspotId)
        }

        protected open fun use(game: IceGame, hotspotId: String) {
        }
    }
}


interface UseWith {
    val useableItems: Set<String>

    fun useWith(game: IceGame, item: String, hotspotId: String)
}
