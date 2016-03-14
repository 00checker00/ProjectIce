package de.project.ice.hotspot.hotspots.scene2

import de.project.ice.IceGame
import de.project.ice.hotspot.Use


class Angelschnur : Use.Adapter() {
    override fun look(game: IceGame) {
        game.showMessages("s3_string_desc");
    }
}