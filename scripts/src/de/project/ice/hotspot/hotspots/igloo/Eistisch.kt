package de.project.ice.hotspot.hotspots.igloo

import de.project.ice.IceGame
import de.project.ice.dialog.Node
import de.project.ice.hotspot.Use


class Eistisch: Use.Adapter() {


    override fun look(game: IceGame, hotspotId: String) {
        game.showAndiMessages("s1_igloo_icedesk_desc")
    }
}