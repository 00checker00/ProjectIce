package de.project.ice.hotspot.hotspots.scene1pan

import de.project.ice.IceGame
import de.project.ice.hotspot.Use


class KlaraIglu : Use.Adapter(){


    override fun look(game: IceGame, hotspotId: String) {
        game.showAndiMessages("s1_panorama_igloo_klara_desc")
    }
}