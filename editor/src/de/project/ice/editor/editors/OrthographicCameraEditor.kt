package de.project.ice.editor.editors

import com.badlogic.gdx.graphics.OrthographicCamera

import java.lang.reflect.Field

class OrthographicCameraEditor : BaseEditor() {
    private var viewportScale = 1.0f
    private var lastViewportScale = 1.0f
    private var camera: OrthographicCamera? = null

    override fun bind(field: Field, target: Any, description: String?): BaseEditor {
        try {
            add(field.name + ":").row()
            camera = field.get(target) as OrthographicCamera
            val position = OrthographicCamera::class.java.getField("position")
            add(Vector3Editor().bind(position, camera!!, "The position of the camera")).padLeft(10f).row()
            val viewportScale = OrthographicCameraEditor::class.java.getDeclaredField("viewportScale")
            add(NumberEditor.FloatEditor().bind(viewportScale, this, "The size of the viewport")).padLeft(10f).row()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        return this
    }

    override fun act(delta: Float) {
        super.act(delta)

        if (lastViewportScale != viewportScale)
        // scale has been changed in Editor
        {
            // Update target values
            camera!!.viewportWidth = 16 * viewportScale
            camera!!.viewportHeight = 9 * viewportScale
        } else {
            val scale = camera!!.viewportWidth / 16f
            if (scale != viewportScale) {
                lastViewportScale = scale
                viewportScale = scale

            }
        }

    }
}
