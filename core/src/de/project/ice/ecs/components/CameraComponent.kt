package de.project.ice.ecs.components

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import de.project.ice.annotations.Property

/**
 * ONLY one camera component should exist
 */
class CameraComponent : CopyableIceComponent {
    @Property("The camera")
    var camera = OrthographicCamera(16f, 9f)
    @Property("Blend color")
    var color = Color.WHITE

    override fun reset() {
        camera = OrthographicCamera()
        camera.viewportWidth = 16f
        camera.viewportHeight = 9f
        color = Color.WHITE
    }

    override fun copyTo(copy: CopyableIceComponent) {
        if (copy is CameraComponent) {
            copy.camera = OrthographicCamera(camera.viewportWidth, camera.viewportHeight)
            copy.camera.position.set(camera.position)
        }
    }
}