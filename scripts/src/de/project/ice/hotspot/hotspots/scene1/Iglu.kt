package de.project.ice.hotspot.hotspots.scene1

import de.project.ice.IceGame
import de.project.ice.hotspot.Use


class Iglu : Use.Adapter(){
    override fun look(game: IceGame, hotspotId: String) {
        game.showAndiMessages("s1_standard_igloo_desc")
    }
}