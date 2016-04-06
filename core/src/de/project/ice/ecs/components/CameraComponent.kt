package de.project.ice.ecs.components

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import de.project.ice.annotations.Property
import de.project.ice.ecs.systems.CameraSystem

/**
 * ONLY one camera component should exist
 */
class CameraComponent : CopyableIceComponent {
    @Property("The camera")
    var camera = OrthographicCamera(16f, 9f)
    @Property("Blend color")
    var color = Color.WHITE

    @Property("How the camera follows the player")
    var followType: CameraSystem.FollowType = CameraSystem.FollowType.None
    @Property("How near the player can go to the screen edge before the camera scrolls")
    var followOffset = 1.0f
    @Property("The minimum X position of the Camera (when it follows the player)")
    var followMinX = Float.MIN_VALUE
    @Property("The maximum X position of the Camera (when it follows the player)")
    var followMaxX = Float.MAX_VALUE

    @Property("The position of the horizon")
    var targetY: Float = 20.0f
    @Property("The scale of the distance scaled entities, when they reach the horizon")
    var targetScale: Float = 1.0f

    override fun reset() {
        camera = OrthographicCamera()
        camera.viewportWidth = 16f
        camera.viewportHeight = 9f
        color = Color.WHITE
        followType = CameraSystem.FollowType.Horizontal
        followOffset = 1.0f
        followMinX = Float.MIN_VALUE
        followMaxX = Float.MAX_VALUE
        targetY = 20.0f
        targetScale = 1.0f
    }

    override fun copyTo(copy: CopyableIceComponent) {
        if (copy is CameraComponent) {
            copy.camera = OrthographicCamera(camera.viewportWidth, camera.viewportHeight)
            copy.camera.position.set(camera.position)
            copy.followType = followType
            copy.followOffset = followOffset
            copy.followMaxX = followMaxX
            copy.followMinX = followMinX
            copy.targetY = targetY
            copy.targetScale = targetScale
        }
    }
}