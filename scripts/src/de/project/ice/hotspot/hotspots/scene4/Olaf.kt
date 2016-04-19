package de.project.ice.hotspot.hotspots.scene4

import de.project.ice.IceGame
import de.project.ice.hotspot.Use


class Olaf : Use.Adapter(){
    override fun look(game: IceGame, hotspotId: String) {
        game.showAndiMessages("s4_olaf_desc")
    }
}