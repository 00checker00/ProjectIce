package de.project.ice.editor.editors

import com.badlogic.gdx.utils.Array
import com.kotcrab.vis.ui.widget.VisSelectBox
import de.project.ice.ecs.components.ActivationDirection
import de.project.ice.editor.editors.ValueEditor
import de.project.ice.screens.CursorScreen

class ActivationDirectionEditor : ValueEditor<ActivationDirection>() {
    private var valueField: VisSelectBox<ActivationDirection> = VisSelectBox()

    override fun createUi() {
        valueField.items = Array(ActivationDirection.values())
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
