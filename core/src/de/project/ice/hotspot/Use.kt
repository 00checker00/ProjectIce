package de.project.ice.hotspot

import de.project.ice.IceGame
import de.project.ice.screens.CursorScreen

interface Use {
    fun use(game: IceGame, cursor: CursorScreen.Cursor)

    abstract class Adapter : Use {
        override fun use(game: IceGame, cursor: CursorScreen.Cursor) {
            when (cursor) {
                CursorScreen.Cursor.None -> {
                }
                CursorScreen.Cursor.Walk -> walk(game)
                CursorScreen.Cursor.Look -> look(game)
                CursorScreen.Cursor.Speak -> speak(game)
                CursorScreen.Cursor.Take -> take(game)
            }
        }

        protected fun walk(game: IceGame) {
        }

        protected open fun look(game: IceGame) {
        }

        protected open fun speak(game: IceGame) {
        }

        protected open fun take(game: IceGame) {
        }
    }

    open class Take(private val entityName: String, private val itemName: String) : Adapter() {

        override fun take(game: IceGame) {
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
        override fun use(game: IceGame, cursor: CursorScreen.Cursor) {
            use(game)
        }

        protected open fun use(game: IceGame) {
        }
    }
}

