package de.project.ice.hotspot.hotspots.igloo

import de.project.ice.IceGame
import de.project.ice.dialog.Node
import de.project.ice.hotspot.Use


class Ofen: Use.Adapter() {
    override fun use(game: IceGame, hotspotId: String) {
        game.showAndiMessages("s1_igloo_oven_use")
    }

    override fun look(game: IceGame, hotspotId: String) {
        game.showAndiMessages("s1_igloo_oven_desc")
    }
}