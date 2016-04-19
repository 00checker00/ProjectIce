package de.project.ice.hotspot.hotspots.scene1

import de.project.ice.IceGame
import de.project.ice.hotspot.GotoScene
import de.project.ice.hotspot.Use


class Hauptiglu : GotoScene("igloo") {

    override fun look(game: IceGame, hotspotId: String) {
        game.showAndiMessages("s1_main_igloo_desc")
    }
}