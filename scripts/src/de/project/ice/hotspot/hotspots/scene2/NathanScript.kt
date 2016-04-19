package de.project.ice.hotspot.hotspots.scene2

import de.project.ice.IceGame
import de.project.ice.hotspot.Use


class NathanScript: Use.Adapter() {
    override fun speak(game: IceGame, hotspotId: String) {
        game.showDialog("s2_dlg_nathan")
    }

    override fun look(game: IceGame, hotspotId: String) {
        game.showAndiMessages("s2_nathan_desc")
    }
}