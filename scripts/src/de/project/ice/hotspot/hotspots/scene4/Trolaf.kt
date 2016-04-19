package de.project.ice.hotspot.hotspots.scene4

import de.project.ice.IceGame
import de.project.ice.hotspot.Use


class Trolaf : Use.Adapter(){
    override fun look(game: IceGame, hotspotId: String) {
        game.showAndiMessages("s4_trolaf_desc")
    }
}