package de.project.ice.hotspot.hotspots.scene4

import de.project.ice.IceGame
import de.project.ice.hotspot.Use


class Wikingeriglu : Use.Adapter(){
    override fun look(game: IceGame, hotspotId: String) {
        game.showMessages("s4_wikinger_igloo_desc")
    }


}