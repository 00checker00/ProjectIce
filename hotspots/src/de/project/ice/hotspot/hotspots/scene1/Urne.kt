package de.project.ice.hotspot.hotspots.scene1

import de.project.ice.IceGame
import de.project.ice.hotspot.Use


class Urne : Use.Adapter(){
    override fun look(game: IceGame) {
        game.showMessages("s1_urn_desc")
    }
}