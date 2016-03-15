package de.project.ice.ecs.components

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Interpolation
import de.project.ice.annotations.Property


class BlendComponent: IceComponentAdapter() {
    @Property("The start color")
    var start = Color.WHITE
    @Property("The target color")
    var target = Color.BLACK
    @Property("The duration of the blend")
    var duration = 2.0f
    @Property("The interpolation method")
    var interpolator: Interpolation = Interpolation.linear
    var current = 0.0f

    override fun reset() {
        super.reset()
        start = Color.WHITE
        target = Color.BLACK
        duration = 2.0f
        current = 0.0f
        interpolator = Interpolation.linear
    }

    override fun copyTo(copy: CopyableIceComponent) {
        if (copy is BlendComponent) {
            copy.target = target
            copy.duration = duration
            copy.target = target
            copy.interpolator = interpolator
            copy.start = start
        }
    }
}