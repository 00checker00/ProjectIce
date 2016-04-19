package de.project.ice.hotspot.hotspots.scene3

import de.project.ice.IceGame
import de.project.ice.hotspot.Use
import de.project.ice.hotspot.UseWith


class Angelschnur : Use.Adapter(), UseWith {

    override val useableItems: Set<String> = hashSetOf("inv_shaft");

    override fun useWith(game: IceGame, item: String, hotspotId: String) {
        throw UnsupportedOperationException()
    }

    override fun look(game: IceGame, hotspotId: String) {
        game.showAndiMessages("s3_ice_floe_desc");
    }
}