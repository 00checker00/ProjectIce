package de.project.ice.hotspot.hotspots.scene4

import de.project.ice.IceGame
import de.project.ice.hotspot.Use


class Brolaf : Use.Adapter(){
    override fun look(game: IceGame) {
        game.showMessages("s4_brolaf_desc")
    }

    override fun speak(game: IceGame) {
        game.showDialog("")
    }
}