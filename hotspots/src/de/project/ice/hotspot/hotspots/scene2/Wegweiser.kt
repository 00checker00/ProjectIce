package de.project.ice.hotspot.hotspots.scene2

import de.project.ice.IceGame
import de.project.ice.hotspot.Use


class Wegweiser : Use.Adapter() {
    override fun look(game: IceGame) {
        game.showMessages("s2_signpost_desc");
    }
}