package de.project.ice.hotspot.hotspots.igloo

import com.badlogic.gdx.graphics.Color
import de.project.ice.IceGame
import de.project.ice.dialog.Node
import de.project.ice.hotspot.Use


class KlaraFall: Use.Adapter() {
    override fun speak(game: IceGame, hotspotId: String) {
        game.showDialog(Node().apply {
            speaker = "Klara Fall"
            type = Node.Type.Text
            color = Color.valueOf("#ee74f0")
            text = "s1_dlg_princess_good_luck"
        })
    }

    override fun look(game: IceGame, hotspotId: String) {
        game.showAndiMessages("s1_klara_fall_desc")
    }
}