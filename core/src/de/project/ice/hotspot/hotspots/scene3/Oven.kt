package de.project.ice.hotspot.hotspots.scene3

import de.project.ice.IceGame
import de.project.ice.Storage
import de.project.ice.ecs.components.InvisibilityComponent
import de.project.ice.hotspot.Use
import de.project.ice.hotspot.UseWith


class Oven: Use.Adapter(), UseWith {
    override val useableItems: Set<String> = hashSetOf("Wood")

    override fun look(game: IceGame) {
        game.showMessages("s3_oven_no_wood")
    }

    override fun useWith(game: IceGame, item: String) {
        game.inventory.removeItem("Wood")
        val fire = game.engine.getEntityByName("oven_fire")
        fire?.remove(InvisibilityComponent::class.java)
        Storage.SAVESTATE.put("scene_03_oven_fire", true)
    }
}