package de.project.ice.hotspot.hotspots.scene3

import de.project.ice.IceGame
import de.project.ice.hotspot.Use


class Eisscholle : Use.Adapter() {
    override fun look(game: IceGame) {
        game.showMessages("s3_ice_floe_desc");
    }
}