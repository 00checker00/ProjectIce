package de.project.ice.ecs.components

import de.project.ice.pathlib.PathArea

class WalkAreaComponent : IceComponent {
    var area = PathArea()

    override fun reset() {
        area = PathArea()
    }
}
