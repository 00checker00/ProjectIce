package de.project.ice.ecs.components

import com.badlogic.gdx.math.Vector2
import de.project.ice.annotations.Property
import de.project.ice.screens.CursorScreen

class HotspotComponent : CopyableIceComponent {
    @Property("The position of the hotspot")
    val origin = Vector2(0f, 0f)
    @Property("Position to which characters walk when interacting with the hotspot. Relative to the origin")
    val targetPos = Vector2(0f, 0f)
    @Property("Width of the hotspot")
    var width = 0f
    @Property("Height of the hotspot")
    var height = 0f
    @Property("Name of the Script (Classname)")
    var script = ""
    @Property("Primary cursor (Left click)")
    var primaryCursor = CursorScreen.Cursor.None
    @Property("Secondary cursor (Right click)")
    var secondaryCursor = CursorScreen.Cursor.None

    override fun reset() {
        origin.set(0f, 0f)
        targetPos.set(0f, 0f)
        width = 0f
        height = 0f
        script = ""
        primaryCursor = CursorScreen.Cursor.None
        secondaryCursor = CursorScreen.Cursor.None
    }

    override fun copyTo(copy: CopyableIceComponent) {
        if (copy is HotspotComponent) {
            copy.origin.set(origin)
            copy.targetPos.set(targetPos)
            copy.width = width
            copy.height = height
            copy.script = script
            copy.primaryCursor = primaryCursor
            copy.secondaryCursor = secondaryCursor
        }
    }
}


