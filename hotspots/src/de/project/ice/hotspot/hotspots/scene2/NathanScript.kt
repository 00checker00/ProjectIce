package de.project.ice.hotspot.hotspots.scene2

import de.project.ice.IceGame
import de.project.ice.hotspot.Use


class NathanScript: Use.Adapter() {
    override fun speak(game: IceGame) {
        game.showDialog("s2_dlg_nathan")



    }
}