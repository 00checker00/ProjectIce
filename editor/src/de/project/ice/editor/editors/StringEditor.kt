package de.project.ice.editor.editors

class StringEditor : ValueEditor<String>() {
    private var valueField: VisTextField? = null

    override fun createUi() {

        valueField = VisTextField(if (value != null) value else "")
        addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                if (button == Input.Buttons.MIDDLE) {
                    valueField!!.text = valueField!!.text + Gdx.app.clipboard.contents
                    return true
                }
                return super.touchDown(event, x, y, pointer, button)
            }
        })
        add<VisTextField>(valueField)
    }

    override fun act(delta: Float) {
        value = valueField!!.text
        super.act(delta)
    }

    override fun updateValue() {
        valueField!!.text = value
    }
}
