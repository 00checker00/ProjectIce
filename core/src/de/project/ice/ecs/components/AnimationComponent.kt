package de.project.ice.ecs.components

import com.badlogic.gdx.utils.IntMap
import de.project.ice.annotations.Property
import de.project.ice.utils.Assets

class AnimationComponent : CopyableIceComponent {
    @Property("The active animation")
    var animation = 0
        set(animation) {
            field = animation
            time = 0.0f
        }
    @Property("The current animation time", true)
    var time = 0.0f

    @Property("The list of animations")
    val animations = IntMap<Assets.Holder.Animation>()

    override fun reset() {
        animations.clear()
        animation = 0
        time = 0.0f
    }

    override fun copyTo(copy: CopyableIceComponent) {
        if (copy is AnimationComponent) {
            copy.animation = animation
            for (animationHolder in animations) {
                val data = animationHolder.value.data
                if (data != null) {
                    copy.animations.put(animationHolder.key,
                            Assets.createAnimation(
                                    animationHolder.value.name,
                                    data.frameDuration,
                                    data.playMode))
                }
            }
            copy.time = time
        }
    }
}