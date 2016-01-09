package de.project.ice.hotspot

import com.badlogic.gdx.utils.ObjectMap
import de.project.ice.IceGame

import de.project.ice.screens.CursorScreen.Cursor

class Hotspot {
    val id: String
    val primaryCursor: Cursor
    val secondaryCursor: Cursor
    val cursorUse: Use
    private val useWithMap: ObjectMap<String, Use>

    private constructor(id: String,
                        primaryCursor: Cursor,
                        secondaryCursor: Cursor,
                        cursorUse: Use,
                        useWithMap: ObjectMap<String, Use>) {
        this.id = id
        this.primaryCursor = primaryCursor
        this.secondaryCursor = secondaryCursor
        this.cursorUse = cursorUse
        this.useWithMap = useWithMap
    }

    constructor(id: String,
                primaryCursor: Cursor,
                secondaryCursor: Cursor,
                cursorUse: Use,
                vararg usages: Pair<String, out Use>) {
        this.id = id
        this.primaryCursor = primaryCursor
        this.secondaryCursor = secondaryCursor
        this.cursorUse = cursorUse
        this.useWithMap = ObjectMap<String, Use>()
        for (usage in usages) {
            useWithMap.put(usage.first, usage.second)
        }
    }

    constructor(id: String,
                primaryCursor: Cursor,
                cursorUse: Use,
                vararg usages: Pair<String, Use>) : this(id, primaryCursor, Cursor.None, cursorUse, *usages) {
    }

    constructor(id: String,
                vararg usages: Pair<String, Use>) : this(id,
            Cursor.None,
            Cursor.None,
            object : Use {
                public override fun use(game: IceGame, cursor: Cursor) {
                }
            },
            *usages) {
    }

    @SuppressWarnings("unchecked")
    constructor(id: String) : this(id,
            Cursor.None,
            Cursor.None,
            object : Use {
                public override fun use(game: IceGame, cursor: Cursor) {
                }
            }) {
    }

    fun canUseWith(item: String): Boolean {
        return useWithMap.containsKey(item)
    }

    fun useWith(game: IceGame, item: String) {
        useWithMap.get(item, object : Use.With() {

        }).use(game, Cursor.None)
    }

    fun use(game: IceGame, cursor: Cursor) {
        cursorUse.use(game, cursor)
    }

    class Builder {
        private var id: String? = null
        private var primaryCursor: Cursor? = null
        private var secondaryCursor = Cursor.None
        private var cursorUse: Use? = null
        private val useWithMap = ObjectMap<String, Use>()

        fun id(id: String): Builder {
            this.id = id
            return this
        }

        fun primaryCursor(primaryCursor: Cursor): Builder {
            this.primaryCursor = primaryCursor
            return this
        }

        fun secondaryCursor(secondaryCursor: Cursor): Builder {
            this.secondaryCursor = secondaryCursor
            return this
        }

        fun use(cursorUse: Use): Builder {
            this.cursorUse = cursorUse
            return this
        }

        fun useWith(withItem: String, use: Use): Builder {
            useWithMap.put(withItem, use)
            return this
        }

        fun build(): Hotspot {
            val id = this.id
            val primaryCursor = this.primaryCursor
            val cursorUse = this.cursorUse

            if (id == null) {
                throw RuntimeException("Trying to build a hotspot without an id")
            }
            if (primaryCursor == null) {
                throw RuntimeException("Trying to build hotspot without a primary cursor")
            }
            if (cursorUse == null) {
                throw RuntimeException("Trying to build hotspot without a HotspotUse")
            }

            val hotspot = Hotspot(id, primaryCursor, secondaryCursor, cursorUse, useWithMap)
            return hotspot
        }
    }
}
