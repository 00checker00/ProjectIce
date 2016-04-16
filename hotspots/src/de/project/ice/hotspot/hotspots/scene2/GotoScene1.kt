package de.project.ice.hotspot.hotspots.scene2


import de.project.ice.IceGame
import de.project.ice.ecs.Components
import de.project.ice.hotspot.GotoScene
import de.project.ice.ecs.getComponents

class GotoScene1 : GotoScene("scene1"){
    override val spawnpoint: String? = "spawn_scene2"
}