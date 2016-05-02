package de.project.ice.hotspot.hotspots.scene1pan

import de.project.ice.IceGame
import de.project.ice.hotspot.Use


class Laterne : Use.Adapter(){

    override fun take(game: IceGame, hotspotId: String) {

        game.showAndiMessages("s1_panorama_lantern_take")
    }

    override fun look(game: IceGame, hotspotId: String) {
        game.showAndiMessages("s1_panorama_lantern_desc")
    }
}