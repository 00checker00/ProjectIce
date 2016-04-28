package de.project.ice.hotspot.hotspots.scene2

import de.project.ice.IceGame
import de.project.ice.ecs.Components
import de.project.ice.ecs.getComponents
import de.project.ice.ecs.systems.AndiAnimation
import de.project.ice.hotspot.Use

class Wurm: Use.Take("Worm", "inv_worm") {
    override fun take(game: IceGame, hotspotId: String) {
        game.playAndiAnimation(AndiAnimation.Buecken) {
            super.take(game, hotspotId)
        }
    }

    override fun look(game: IceGame, hotspotId: String) {
        game.showAndiMessages("s2_worm_desc")
    }
}