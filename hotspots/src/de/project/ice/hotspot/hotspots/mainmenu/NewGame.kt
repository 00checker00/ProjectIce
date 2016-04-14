package de.project.ice.hotspot.hotspots.mainmenu

import de.project.ice.IceGame
import de.project.ice.ecs.Components
import de.project.ice.ecs.components.PathPlanningComponent
import de.project.ice.ecs.getComponent
import de.project.ice.hotspot.Use
import de.project.ice.screens.CursorScreen


class NewGame : Use {
    override fun use(game: IceGame, cursor: CursorScreen.Cursor, hotspotId: String) {
        val andi = game.engine.getEntityByName("Andi_Player")
        val start = andi?.getComponent(Components.transform)?.pos
        val target = game.engine.getEntityByName("out_newGame")?.getComponent(Components.transform)?.pos

        if(target!=null && andi!=null && start!=null) {
            val pathComponent = game.engine.createComponent(PathPlanningComponent::class.java)
            pathComponent.target = target
            pathComponent.start = start
            andi.add(pathComponent)
        }
    }
}