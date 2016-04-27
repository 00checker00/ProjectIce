package de.project.ice.hotspot.hotspots.scene2


import de.project.ice.IceGame
import de.project.ice.ecs.Components
import de.project.ice.hotspot.GotoScene
import de.project.ice.ecs.getComponents

class GotoPanorama : GotoScene("scene1_panorama"){
    override val spawnpoint: String? = "spawn_scene2"
}