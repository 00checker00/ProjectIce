package de.project.ice.editor

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Array
import de.project.ice.config.Config.PIXELS_TO_METRES
import de.project.ice.ecs.Components
import de.project.ice.ecs.Families
import de.project.ice.ecs.components.TransformComponent
import de.project.ice.ecs.systems.RenderingSystem
import de.project.ice.editor.undoredo.ModifySceneAction
import de.project.ice.screens.BaseScreenAdapter

class EditGameScreen(app: EditorApplication) : BaseScreenAdapter(app) {
    override val priority = 3

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
                    val entities = Array(app.engine.getEntitiesFor(Families.renderable).toArray())
                    entities.sort(RenderingSystem.RenderingComparator())
                    entities.reverse()
                    for (entity in entities) {
                        val transform = Components.transform.get(entity)
                        val texture = Components.texture.get(entity)

                        if (!texture.region.isValid) {
                            continue
                        }

                        val width = texture.region.data!!.regionWidth * PIXELS_TO_METRES
                        val height = texture.region.data!!.regionHeight * PIXELS_TO_METRES

                        if (Rectangle(transform.pos.x, transform.pos.y, width, height).contains(coords.x, coords.y)) {
                            dragComponent = transform
                            dragOriginX = coords.x - transform.pos.x
                            dragOriginY = coords.y - transform.pos.y
                            return false
                        }
                    }
                    return false
                } else if (button == Input.Buttons.MIDDLE) {
                    cameraDragDown.set(screenX.toFloat(), screenY.toFloat())
                    cameraDrag = cameraComponent.camera
                }
            }
            return false
        }

        override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
            if (dragComponent != null) {
                val cameras = app.engine.getEntitiesFor(Families.camera)
                if (cameras.size() == 0) {
                    return false
                }

                val activeCamera = cameras.first()
                val cameraComponent = Components.camera.get(activeCamera)
                val coords = cameraComponent.camera.unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0f))

                dragComponent!!.pos.set(Vector2(coords.x - dragOriginX, coords.y - dragOriginY))
            } else if (cameraDrag != null) {
                cameraDrag!!.translate(Vector2(screenX.toFloat(), screenY.toFloat()).sub(cameraDragDown).scl(PIXELS_TO_METRES).scl(-1f, 1f))
                cameraDragDown.set(screenX.toFloat(), screenY.toFloat())
            }
            return true
        }
    }
}
