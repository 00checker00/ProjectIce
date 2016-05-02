package de.project.ice.hotspot.hotspots.scene1pan

import de.project.ice.IceGame
import de.project.ice.hotspot.Use


class Lederschuhe : Use.Adapter(){

    override fun take(game: IceGame, hotspotId: String) {

        game.showAndiMessages("s1_panorama_leather_boots_take")
    }

    override fun look(game: IceGame, hotspotId: String) {
        game.showAndiMessages("s1_panorama_leather_boots_desc")
    }
}