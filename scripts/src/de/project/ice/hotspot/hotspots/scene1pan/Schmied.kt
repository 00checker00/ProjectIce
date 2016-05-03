package de.project.ice.hotspot.hotspots.scene1pan

import com.badlogic.gdx.graphics.Color
import de.project.ice.IceGame
import de.project.ice.dialog.Node
import de.project.ice.hotspot.Use


class Schmied : Use.Adapter(){


    override fun look(game: IceGame, hotspotId: String) {
        game.showAndiMessages("s1_panorama_smithy_desc")
    }

    override fun speak(game: IceGame, hotspotId: String) {
        val dialog = Node().apply {
            speaker = hotspotId
            type = Node.Type.Text
            text = "s1_dlg_smithy"
            color = Color.valueOf("#994C00")
        }
        game.showDialog(dialog)
    }


}
