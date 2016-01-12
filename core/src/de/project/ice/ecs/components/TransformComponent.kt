package de.project.ice.ecs.components

import com.badlogic.gdx.math.Vector2

class TransformComponent : CopyableIceComponent {
    val pos = Vector2()
    val scale = Vector2(1.0f, 1.0f)
    var rotation = 0.0f
    var z = 0
    var flipHorizontal: Boolean = false
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
