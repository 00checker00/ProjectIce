package de.project.ice.hotspot.hotspots.scene2

import de.project.ice.IceGame
import de.project.ice.ecs.Components
import de.project.ice.ecs.getComponent
import de.project.ice.hotspot.Use


class GetauteStelle: Use.Adapter() {
    override fun look(game: IceGame, hotspotId: String) {
        game.showMessages("s2_wormhole_desc")
    }



}