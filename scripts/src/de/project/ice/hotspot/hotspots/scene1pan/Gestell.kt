package de.project.ice.hotspot.hotspots.scene1pan

import de.project.ice.IceGame
import de.project.ice.hotspot.Use


class Gestell : Use.Adapter(){

    override fun take(game: IceGame, hotspotId: String) {

        game.showAndiMessages("s1_panorama_fishracks_take")
    }

    override fun look(game: IceGame, hotspotId: String) {
        game.showAndiMessages("s1_panorama_fishracks_desc")
    }
}