package de.project.ice.hotspot.hotspots.scene1

import de.project.ice.IceGame
import de.project.ice.ecs.Components
import de.project.ice.ecs.getComponents
import de.project.ice.hotspot.Use
import de.project.ice.scripting.runOnce


class Brunnen : Use.Adapter(){
    override fun look(game: IceGame, hotspotId: String) {
        game.showMessages("s1_well_desc")
    }

    override fun use(game: IceGame, hotspotId: String) {

        val andi = game.engine.getEntityByName("Andi_Player")
        val animation = andi?.getComponents(Components.animation)

        animation?.animation = 3
        var duration = animation?.animations?.get(3)?.data?.animationDuration?: 0.0f

        game.engine.timeout( duration-0.5f , { super.take(game, hotspotId)})

        runOnce("note_taken"){
            game.inventory.addItem("inv_note_shred_4")
        }
    }
}