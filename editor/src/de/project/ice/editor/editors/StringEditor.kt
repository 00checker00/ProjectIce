package de.project.ice.editor.editors

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.kotcrab.vis.ui.widget.VisTextField;

class StringEditor : ValueEditor<String>() {
    private var valueField: VisTextField = VisTextField()

    override fun createUi() {
        valueField.text = if (value != null) value else ""
        addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                if (button == Input.Buttons.MIDDLE) {
                    valueField.text += Gdx.app.clipboard.contents
                    return true
                }
                return super.touchDown(event, x, y, pointer, button)
            }
        })
        add<VisTextField>(valueField)
    }

    override fun act(delta: Float) {
        value = valueField.text
        super.act(delta)
    }

    override fun updateValue() {
        valueField.text = value
    }
}
