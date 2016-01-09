package de.project.ice.ecs.components

import com.badlogic.ashley.core.Entity
import de.project.ice.inventory.Inventory
import de.project.ice.screens.CursorScreen

class UseComponent : CopyableIceComponent {
    var target: Entity? = null
    var cursor: CursorScreen.Cursor = CursorScreen.Cursor.None
    var withItem: Inventory.Item? = null

    override fun reset() {
        target = null
        withItem = null
        cursor = CursorScreen.Cursor.None
    }

    override fun copyTo(copy: CopyableIceComponent) {
        if (copy is UseComponent) {
            copy.target = target
            copy.cursor = cursor
            copy.withItem = withItem
        }
    }
}
