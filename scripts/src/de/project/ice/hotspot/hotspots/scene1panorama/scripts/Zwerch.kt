package de.project.ice.hotspot.hotspots.scene1panorama.scripts

import de.project.ice.IceGame
import de.project.ice.hotspot.Use

class Zwerch: Use.Adapter() {
    override fun look(game: IceGame, hotspotId: String) {
        game.showAndiMessages("s1_panorama_zwerch_desc")
    }

    override fun speak(game: IceGame, hotspotId: String) {

    }
}