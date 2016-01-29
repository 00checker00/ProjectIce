package de.project.ice.editor.editors


import java.lang.reflect.Field

open class ValueEditor<T> : BaseEditor() {
    private var title: String? = null
    protected var value: T? = null
    private var oldValue: T? = null
    protected var field: Field? = null
    protected var target: Any? = null
    protected var fieldDescription: String = ""
        private set

    override fun act(delta: Float) {
        super.act(delta)

        if ((oldValue != null && oldValue != value) || (oldValue == null && value != null)) {
            oldValue = value
            BaseEditor.Companion.setValue(field!!, target!!, value!!, setter)
            fireValueChanged()
        } else {
            try {
                if (value != BaseEditor.Companion.getValue(field!!, target!!, getter)) {
                    value = BaseEditor.Companion.getValue(field!!, target!!, getter) as T?
                    updateValue()
                }
            } catch (ignore: NullPointerException) {
            }

        }
    }

    protected open fun updateValue() {

    }

    override fun bind(field: Field, target: Any, description: String?): ValueEditor<T> {
        this.target = target
        this.field = field
        this.fieldDescription = description ?: ""
        field.isAccessible = true
        try {
            this.value = field.get(target) as T
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        this.oldValue = value
        this.title = field.name + ": "

        defaults().align(Align.left)
        val titleLabel = VisLabel(title)
        if (description != null) {
            Tooltip.Builder(description).target(titleLabel).build()
        }
        add(titleLabel)
        createUi()
        return this
    }

    protected open fun createUi() {

    }

    protected fun fireValueChanged() {
        try {
            val t = value
            field?.set(target, t)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

    }
}
