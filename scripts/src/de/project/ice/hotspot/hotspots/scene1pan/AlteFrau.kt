package de.project.ice.hotspot.hotspots.scene1pan

import com.badlogic.gdx.graphics.Color
import de.project.ice.IceGame
import de.project.ice.dialog.Node
import de.project.ice.hotspot.Use


class AlteFrau : Use.Adapter(){


    override fun look(game: IceGame, hotspotId: String) {
        game.showAndiMessages("s1_panorama_oldlady_desc")
    }

    override fun speak(game: IceGame, hotspotId: String) {
         val dialog = Node().apply {
            speaker = hotspotId
            type = Node.Type.Text
            text = "s1_dlg_oldlady"
             color = Color.valueOf("#666600")
            }
            game.showDialog(dialog)
        }


    }
