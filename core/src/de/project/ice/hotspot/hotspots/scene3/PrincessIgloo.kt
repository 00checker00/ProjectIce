package de.project.ice.hotspot.hotspots.scene3

import de.project.ice.IceGame
import de.project.ice.Storage
import de.project.ice.hotspot.Use
import de.project.ice.hotspot.UseWith


class PrincessIgloo: Use.Adapter(), UseWith {
    override val useableItems: Set<String> = hashSetOf("Teapot")

    override fun speak(game: IceGame) {
        game.showDialog("s2_dlg_nathan")
    }

    override fun look(game: IceGame) {
        game.showMessages("s3_princess_desc")
    }

    override fun useWith(game: IceGame, item: String) {
        Storage.SAVESTATE.put("PrincessGiveTea", true)
        game.showDialog("PrincessIgloo")
        game.inventory.removeItem("Teapot")
        game.engine.controlSystem.active_item = null
    }
}