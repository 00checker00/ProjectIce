package de.project.ice.ecs.components

import com.badlogic.ashley.core.Entity
import de.project.ice.annotations.Property


class SpeakComponent: CopyableIceComponent {
    @Property("The spoken text")
    var text: String = ""
    @Property("Name of the speaker")
    var targetName: String = ""
    var target: Entity? = null

    override fun reset() {
        text = ""
        target = null
        targetName = ""
    }

    override fun copyTo(copy: CopyableIceComponent) {
        if (copy is SpeakComponent) {
            copy.text = text
            copy.target = target
            copy.targetName = targetName
        }
    }
}