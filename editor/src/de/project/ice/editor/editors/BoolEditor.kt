package de.project.ice.editor.editors

import com.kotcrab.vis.ui.widget.VisCheckBox

class BoolEditor : ValueEditor<Boolean>() {
    private var valueField: VisCheckBox = VisCheckBox("")

    override fun createUi() {
        valueField.isChecked = value ?: false
        add(valueField)
    }

    override fun act(delta: Float) {
        value = valueField.isChecked
        super.act(delta)
    }

    override fun updateValue() {
        valueField.isChecked = value ?: false
    }
}
