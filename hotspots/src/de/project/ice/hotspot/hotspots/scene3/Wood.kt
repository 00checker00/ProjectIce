package de.project.ice.hotspot.hotspots.scene3

import de.project.ice.IceGame
import de.project.ice.hotspot.Use

class Wood: Use.Take("wood", "Wood") {
    override fun look(game: IceGame) {
        game.showMessages("s3_wood_desc")
    }
}
