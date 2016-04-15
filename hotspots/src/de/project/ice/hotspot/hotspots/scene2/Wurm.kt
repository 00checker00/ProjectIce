package de.project.ice.hotspot.hotspots.scene2

import de.project.ice.IceGame
import de.project.ice.ecs.Components
import de.project.ice.ecs.getComponent
import de.project.ice.hotspot.Use


class Wurm: Use.Take("Worm", "inv_worm") {
    override fun take(game: IceGame, hotspotId: String) {
        val andi = game.engine.getEntityByName("Andi_Player")
        val animation = andi?.getComponent(Components.animation)

        animation?.animation = 3
        var duration = animation?.animations?.get(3)?.data?.animationDuration?: 0.0f

        game.engine.timeout( duration-0.5f , { super.take(game, hotspotId)})
    }

    override fun look(game: IceGame, hotspotId: String) {
        game.showMessages("s2_wormhole_desc")
    }
}