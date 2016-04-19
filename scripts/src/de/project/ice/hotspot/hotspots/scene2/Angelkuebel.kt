package de.project.ice.hotspot.hotspots.scene2

import de.project.ice.IceGame
import de.project.ice.Storage
import de.project.ice.hotspot.Use
import de.project.ice.scripting.runOnce


class Angelkuebel : Use.Take("", "inv_fishing_rod") {
    override fun look(game: IceGame, hotspotId: String) {
        game.showAndiMessages("s2_fishing_rod_cylinder_desc");
    }

    override fun take(game: IceGame, hotspotId: String) {

        if(Storage.SAVESTATE.getString("s2_angelrute")=="true") {
        //    super.take(game)
        } else {
            game.showAndiMessages("s2_fishing_rod_theft")
        }

    }
}