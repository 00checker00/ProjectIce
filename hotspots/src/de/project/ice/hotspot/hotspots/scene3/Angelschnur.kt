package de.project.ice.hotspot.hotspots.scene2

import de.project.ice.IceGame
import de.project.ice.hotspot.Use
import de.project.ice.hotspot.UseWith


class Angelschnur : UseWith {

    override val useableItems: Set<String> = hashSetOf("inv_shaft");

    override fun useWith(game: IceGame, item: String) {


        throw UnsupportedOperationException()
    }

    override fun look(game: IceGame) {
        game.showMessages("s3_ice_floe_desc");
    }
}