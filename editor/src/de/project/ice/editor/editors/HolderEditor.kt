package de.project.ice.editor.editors

import de.project.ice.utils.Assets

abstract class HolderEditor<T> : ValueEditor<Assets.Holder<T>>() {
    private var valueField: VisTextField? = null

    protected val holderName: String
        get() {
            val value = this.value
            if (value != null) {
                return value.name
            }
            return "invalid"
        }

    protected var holderData: Assets.Holder<T>?
        get() {
            if (value != null) {
                return value
            }
            return null
        }
        set(data) {
            if (value != null && data != null) {
                val dataField = Assets.Holder::class.java.getDeclaredField("data")
                dataField.isAccessible = true
                dataField.set(value, data.data)

                val nameField = Assets.Holder::class.java.getDeclaredField("name")
                nameField.isAccessible = true
                nameField.set(value, data.name)
            }
        }

    override fun createUi() {
        valueField = VisTextField(holderName)
        valueField!!.isDisabled = true
        add<VisTextField>(valueField)
        val editButton = VisTextButton("...", object : ChangeListener() {
            override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                onEdit()
            }
        })
        add(editButton)
    }

    protected open fun onEdit() {

    }

    override fun act(delta: Float) {
        super.act(delta)
        valueField!!.text = holderName
    }

}
