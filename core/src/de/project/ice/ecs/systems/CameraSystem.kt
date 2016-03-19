package de.project.ice.ecs.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector3
import de.project.ice.ecs.Components
import de.project.ice.ecs.IceEngine
import de.project.ice.ecs.components.CameraComponent
import de.project.ice.ecs.getComponent

/**
 * all camera settings here
 * creation, settings
 */
class CameraSystem() : IceSystem() {
    enum class FollowType {
        None,
        Horizontal
    }

    override fun addedToEngine(engine: IceEngine) {
        super.addedToEngine(engine)
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        val camera = engine?.renderingSystem?.activeCamera;
        val andi = engine?.getEntityByName("Andi_Player")

        if (andi != null && camera != null && camera.followType == FollowType.Horizontal) {
            val minOff = camera.followOffset
            val minX = camera.followMinX
            val maxX = camera.followMaxX

            val camera = camera.camera;
            val pos = andi.getComponent(Components.transform).pos
            val dir = camera.position.cpy().sub(Vector3(pos.x, camera.position.y, 0.0f));
            val edgeOffset = camera.viewportWidth/2 - dir.len()

            if (edgeOffset < minOff) {
                var newX = pos.x + dir.nor().scl(camera.viewportWidth/2-minOff).x;
                if (newX < minX)
                    newX = minX
                else if (newX > maxX)
                    newX = maxX

                camera.position.x = newX
            }
        }
    }
}
