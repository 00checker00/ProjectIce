package de.project.ice.hotspot.hotspots.scene1

import de.project.ice.IceGame
import de.project.ice.hotspot.Use


class ReinFall : Use.Adapter(){
    override fun look(game: IceGame) {
        game.showMessages("s1_rein_fall_desc")
    }

    override fun speak(game: IceGame) {
        game.showDialog("");
    }
}