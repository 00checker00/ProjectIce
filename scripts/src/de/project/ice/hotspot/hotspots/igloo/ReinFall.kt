package de.project.ice.hotspot.hotspots.igloo

import com.badlogic.gdx.graphics.Color
import de.project.ice.IceGame
import de.project.ice.dialog.Node
import de.project.ice.hotspot.Use

class ReinFall: Use.Adapter() {
    override fun speak(game: IceGame, hotspotId: String) {
        game.showDialog(Node().apply {
            speaker = "Rein Fall"
            type = Node.Type.Text
            color = Color.valueOf("#855c34")
            text = "s1_dlg_rein_fall_igloo"
        })
    }

    override fun look(game: IceGame, hotspotId: String) {
        game.showAndiMessages("s1_rein_fall_desc")
    }
}