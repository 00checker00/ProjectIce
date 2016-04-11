package de.project.ice.editor

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import de.project.ice.config.Config.PIXELS_TO_METRES
import de.project.ice.ecs.Components
import de.project.ice.ecs.Families
import de.project.ice.ecs.components.TransformComponent
import de.project.ice.ecs.getComponent
import de.project.ice.editor.undoredo.ModifySceneAction
import de.project.ice.screens.BaseScreenAdapter

class EditGameScreen(app: EditorApplication) : BaseScreenAdapter(app) {
    override val priority = 3
    var selectedEntity: Entity? = null;

    override val inputProcessor: InputProcessor =  object : InputAdapter() {
        private var sceneRecording: ModifySceneAction.Recording? = null
        private var dragComponent: TransformComponent? = null
        private var dragOriginX = 0f
        private var dragOriginY = 0f
        private val cameraDragDown = Vector2()
        private var cameraDrag: OrthographicCamera? = null

        override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
            dragComponent = null
            cameraDrag = null
            sceneRecording?.let {
                app.undoManager.addAction(it.finish())
                sceneRecording = null
            }
            return false
        }

        override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
            if (app.isGamePaused) {
                val cameras = app.engine.getEntitiesFor(Families.camera)
                if (cameras.size() == 0) {
                    return false
                }

                sceneRecording = ModifySceneAction.startRecording(app.engine, app.sceneProperties)

                val activeCamera = cameras.first()
                val cameraComponent = Components.camera.get(activeCamera)
                val coords = cameraComponent.camera.unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0f))
                if (button == Input.Buttons.LEFT) {
                    dragOriginX = coords.x
                    dragOriginY = coords.y
                    return false
                } else if (button == Input.Buttons.MIDDLE) {
                    cameraDragDown.set(screenX.toFloat(), screenY.toFloat())
                    cameraDrag = cameraComponent.camera
                }
            }
            return false
        }

        override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
            if (selectedEntity != null) {
                val cameras = app.engine.getEntitiesFor(Families.camera)
                if (cameras.size() == 0) {
                    return false
                }

                val activeCamera = cameras.first()
                val cameraComponent = Components.camera.get(activeCamera)
                val coords = cameraComponent.camera.unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0f))

                val dx = coords.x - dragOriginX
                val dy = coords.y - dragOriginY

                selectedEntity?.getComponent(Components.transform)?.pos?.add(dx, dy)

                dragOriginX = coords.x
                dragOriginY = coords.y

            } else if (cameraDrag != null) {
                cameraDrag!!.translate(Vector2(screenX.toFloat(), screenY.toFloat()).sub(cameraDragDown).scl(PIXELS_TO_METRES).scl(-1f, 1f))
                cameraDragDown.set(screenX.toFloat(), screenY.toFloat())
            }
            return true
        }
    }
}
