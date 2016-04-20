package de.project.ice.hotspot.hotspots.igloo

import de.project.ice.IceGame
import de.project.ice.dialog.Node
import de.project.ice.hotspot.Use


class Holz: Use.Adapter() {
    override fun take(game: IceGame, hotspotId: String) {
        game.showAndiMessages("s1_igloo_wood_theft")
    }

    override fun look(game: IceGame, hotspotId: String) {
        game.showAndiMessages("s1_igloo_wood_desc")
    }
}