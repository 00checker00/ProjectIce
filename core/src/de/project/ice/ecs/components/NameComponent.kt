package de.project.ice.ecs.components

import de.project.ice.annotations.Property


class NameComponent : CopyableIceComponent {
    @Property("The name of this entity")
    var name = ""

    override fun reset() {
        name = ""
    }

    override fun copyTo(copy: CopyableIceComponent) {
        if (copy is NameComponent) {
            copy.name = name
        }
    }
}
