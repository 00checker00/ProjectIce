package de.project.ice.hotspot.hotspots.igloo

import de.project.ice.IceGame
import de.project.ice.hotspot.Use


class Teekanne: Use.Adapter() {
    override fun take(game: IceGame, hotspotId: String) {
        game.showAndiMessages("s1_igloo_teapot_theft")
    }

    override fun look(game: IceGame, hotspotId: String) {
        game.showAndiMessages("s1_igloo_teapot_desc")
    }
}