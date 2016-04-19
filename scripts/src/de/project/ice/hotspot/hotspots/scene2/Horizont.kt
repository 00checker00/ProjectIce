package de.project.ice.hotspot.hotspots.scene2

import de.project.ice.IceGame
import de.project.ice.hotspot.Use

/**
 * Created by tony on 14.03.16.
 */
class Horizont : Use.Adapter(){

    override fun look(game: IceGame, hotspotId: String) {
      //  game.showDialog("s2")
        game.showAndiMessages("s2_horizon_desc")
    }
}
