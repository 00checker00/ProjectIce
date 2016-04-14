package de.project.ice.hotspot.hotspots.scene1

import de.project.ice.IceGame
import de.project.ice.dialog.Node
import de.project.ice.hotspot.Use
import de.project.ice.screens.DialogScreen


class Teilnehmer : Use.Adapter(){
    override fun look(game: IceGame, hotspotId: String) {
        game.showMessages("s1_participants_desc")
    }

    override fun speak(game: IceGame, hotspotId: String) {
        val dialog = Node().apply {
            speaker = hotspotId
            text = "s1_dlg_participants"
        }
        game.showDialog(dialog)
    }


}