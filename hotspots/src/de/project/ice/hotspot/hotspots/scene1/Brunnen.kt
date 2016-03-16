package de.project.ice.hotspot.hotspots.scene1

import de.project.ice.IceGame
import de.project.ice.ecs.Components
import de.project.ice.ecs.getComponent
import de.project.ice.hotspot.Use


class Brunnen : Use.Adapter(){
    override fun look(game: IceGame) {
        game.showMessages("s1_well_desc")
    }

    override fun use(game: IceGame) {

        val andi = game.engine.getEntityByName("Andi_Player")
        val animation = andi?.getComponent(Components.animation)

        animation?.animation = 3
        var duration = animation?.animations?.get(3)?.data?.animationDuration?: 0.0f

        game.engine.timeout( duration-0.5f , { super.take(game)})

    }
}