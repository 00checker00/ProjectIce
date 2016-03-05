package de.project.ice.editor.editors

import com.badlogic.gdx.utils.Array
import com.kotcrab.vis.ui.widget.VisSelectBox
import de.project.ice.screens.CursorScreen

class CursorEditor : ValueEditor<CursorScreen.Cursor>() {
    private var valueField: VisSelectBox<CursorScreen.Cursor> = VisSelectBox()

    override fun createUi() {
        valueField.items = Array(CursorScreen.Cursor.values())
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
