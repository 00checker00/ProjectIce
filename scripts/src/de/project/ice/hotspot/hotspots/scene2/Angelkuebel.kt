package de.project.ice.hotspot.hotspots.scene2

import de.project.ice.IceGame
import de.project.ice.Storage
import de.project.ice.ecs.Components
import de.project.ice.ecs.getComponents
import de.project.ice.ecs.systems.AndiAnimation
import de.project.ice.hotspot.Use
import de.project.ice.scripting.runOnce
import de.project.ice.utils.Assets


class Angelkuebel : Use.Adapter() {
    override fun look(game: IceGame, hotspotId: String) {
        game.showAndiMessages("s2_fishing_rod_cylinder_desc");
    }

    override fun take(game: IceGame, hotspotId: String) {

        if(Storage.SAVESTATE.getString("s2_angelrute")=="true") {
            runOnce("angelRute") {
                game.inventory.addItem("inv_fishing_rod")

                game.playAndiAnimation(AndiAnimation.Greifen) {
                    game.engine.getEntityByName("Angelkuebel")?.getComponents(Components.texture)?.region = Assets.findRegion("angelkorb2_scene2")
                }

            }

        } else {
            game.showAndiMessages("s2_fishing_rod_theft")
        }

    }
}