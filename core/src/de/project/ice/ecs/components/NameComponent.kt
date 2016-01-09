package de.project.ice.ecs.components


class NameComponent : CopyableIceComponent {
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
