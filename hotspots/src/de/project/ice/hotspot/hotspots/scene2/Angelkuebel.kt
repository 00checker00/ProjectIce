package de.project.ice.hotspot.hotspots.scene2

import de.project.ice.IceGame
import de.project.ice.hotspot.Use


class Angelkuebel : Use.Adapter() {
    override fun look(game: IceGame) {
        game.showMessages("s2_fishing_rod_cylinder_desc");
    }

    override fun take(game: IceGame) {
        game.showMessages("s2_fishing_rod_theft")
    }
}