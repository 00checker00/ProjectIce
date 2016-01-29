package de.project.ice.ecs.components

import de.project.ice.annotations.Property

/**
 * ONLY one camera component should exist
 */
class CameraComponent : CopyableIceComponent {
    @Property("The camera")
    var camera = OrthographicCamera(16f, 9f)

    override fun reset() {
        camera = OrthographicCamera()
        camera.viewportWidth = 16f
        camera.viewportHeight = 9f
        camera.javaClass
    }

    override fun copyTo(copy: CopyableIceComponent) {
        if (copy is CameraComponent) {
            copy.camera = OrthographicCamera(camera.viewportWidth, camera.viewportHeight)
            copy.camera.position.set(camera.position)
        }
    }
}