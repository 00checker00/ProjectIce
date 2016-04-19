package de.project.ice.hotspot.hotspots.scene2

import de.project.ice.IceGame
import de.project.ice.hotspot.Use


class Schneemann : Use.Adapter() {
    override fun speak(game: IceGame, hotspotId: String) {
        game.showAndiMessages("s2_snowman_talk")
    }

    override fun look(game: IceGame, hotspotId: String) {
        game.showAndiMessages("s2_snowman_desc");
    }
}