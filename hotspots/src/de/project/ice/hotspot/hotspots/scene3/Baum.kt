package de.project.ice.hotspot.hotspots.scene3

import de.project.ice.IceGame
import de.project.ice.hotspot.Use


class Baum : Use.Adapter() {
    override fun look(game: IceGame, hotspotId: String) {
        game.showMessages("s3_tree_desc");
    }
}