package de.project.ice.ecs.components

import com.badlogic.gdx.math.Vector2
import de.project.ice.annotations.Property

class TransformComponent : CopyableIceComponent {
    @Property("The position of this Entity")
    val pos = Vector2()
    @Property("The scale of this Entity")
    val scale = Vector2(1.0f, 1.0f)
    @Property("The rotation of this Entity")
    var rotation = 0.0f
    @Property("The Z-Index of this Entity. 0 is the topmost Index")
    var z = 0
    @Property("Whether this Entity is flipped Horizontal")
    var flipHorizontal: Boolean = false
    @Property("Whether this Entity is flipped Vertical")
    var flipVertical: Boolean = false

    override fun reset() {
        pos.set(0f, 0f)
        scale.set(1f, 1f)
        rotation = 0.0f
        z = 0
        flipHorizontal = false
        flipVertical = false
    }

    override fun copyTo(copy: CopyableIceComponent) {
        if (copy is TransformComponent) {
            copy.pos.set(pos)
            copy.scale.set(scale)
            copy.rotation = rotation
            copy.z = z
            copy.flipHorizontal = flipHorizontal
            copy.flipVertical = flipVertical
        }
    }
}
