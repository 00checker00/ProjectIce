package de.project.ice.hotspot.hotspots.scene2

import de.project.ice.IceGame
import de.project.ice.ecs.components.AnimationComponent
import de.project.ice.hotspot.Use


class Wurm: Use.Take("Worm", "Koederwurm") {
    override fun take(game: IceGame) {
        super.take(game)
        game.engine.getEntityByName("Andi_Player")?.getComponent(AnimationComponent::class.java)?.animation = 3
    }

    override fun look(game: IceGame) {
        game.showMessages("s2_wormhole_desc")
    }
}