package de.project.ice.hotspot.hotspots.scene1

import de.project.ice.IceGame
import de.project.ice.dialog.Node
import de.project.ice.hotspot.Use


class Trolaf : Use.Adapter(){


    override fun look(game: IceGame, hotspotId: String) {
        game.showAndiMessages("s1_trolaf_desc")
    }

    override fun speak(game: IceGame, hotspotId: String) {
         val dialog = Node().apply {
            speaker = hotspotId
            type = Node.Type.Text
            text = "s1_dlg_participants"
            }
            game.showDialog(dialog)
        }


    }
