package de.project.ice.editor.editors

import com.badlogic.gdx.utils.Array
import com.kotcrab.vis.ui.widget.VisSelectBox
import de.project.ice.ecs.systems.CameraSystem

class FollowTypeEditor : ValueEditor<CameraSystem.FollowType>() {
    private var valueField: VisSelectBox<CameraSystem.FollowType> = VisSelectBox()

    override fun createUi() {
        valueField.items = Array(CameraSystem.FollowType.values())
        valueField.selected = value
        add(valueField)
    }

    override fun act(delta: Float) {
        value = valueField.selected
        super.act(delta)
    }

    override fun updateValue() {
        valueField.selected = value
    }
}
