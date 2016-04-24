package de.project.ice.ecs.components

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import de.project.ice.annotations.Property


class SpeakComponent: CopyableIceComponent {
    @Property("The spoken text")
    var text: String = ""
    @Property("Name of the speaker")
    var targetName: String = ""
    @Property("The color of the test")
    var color: Color = Color.BLACK
    var target: Entity? = null

    override fun reset() {
        text = ""
        target = null
        targetName = ""
        color = Color.BLACK
    }

    override fun copyTo(copy: CopyableIceComponent) {
        if (copy is SpeakComponent) {
            copy.text = text
            copy.target = target
            copy.targetName = targetName
            copy.color = color
        }
    }
}