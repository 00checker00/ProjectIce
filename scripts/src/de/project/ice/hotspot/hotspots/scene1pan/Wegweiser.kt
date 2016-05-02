package de.project.ice.hotspot.hotspots.scene1pan

import de.project.ice.IceGame
import de.project.ice.hotspot.Use


class Wegweiser : Use.Adapter(){


    override fun look(game: IceGame, hotspotId: String) {
        game.showAndiMessages("s1_panorama_signpost_desc")
    }
}