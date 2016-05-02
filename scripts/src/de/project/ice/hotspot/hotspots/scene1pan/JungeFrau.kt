package de.project.ice.hotspot.hotspots.scene1pan

import com.badlogic.gdx.graphics.Color
import de.project.ice.IceGame
import de.project.ice.dialog.Node
import de.project.ice.hotspot.Use


class JungeFrau : Use.Adapter(){


    override fun look(game: IceGame, hotspotId: String) {
        game.showAndiMessages("s1_panorama_younglady_desc")
    }

    override fun speak(game: IceGame, hotspotId: String) {
         val dialog = Node().apply {
            speaker = hotspotId
            type = Node.Type.Text
            text = "s1_dlg_younglady"
             color = Color.valueOf("#FFFF99")
            }
            game.showDialog(dialog)
        }


    }
