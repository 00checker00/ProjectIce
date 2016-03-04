package de.project.ice.editor.editors


import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.VisTable
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

open class BaseEditor : VisTable() {
    protected var setter: Method? = null
    protected var getter: Method? = null

    init {
        defaults().align(Align.topLeft)
        create()
    }

    protected fun create() {

    }

    open fun bind(field: Field, target: Any, description: String?): BaseEditor {
        val name = field.name
        field.isAccessible = true

        try {
            // Try to find setter for the field
            val setterName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1)
            setter = target.javaClass.getMethod(setterName, field.type)
            setter!!.isAccessible = true
        } catch (ignore: NoSuchMethodException) {
            // No setter
        }

        try {
            // Try to find getter for the field
            val setterName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1)
            getter = target.javaClass.getMethod(setterName, field.type)
            getter!!.isAccessible = true
        } catch (ignore: NoSuchMethodException) {
            // No getter
        }

        return this
    }

    companion object {

        operator fun setValue(field: Field, target: Any, value: Any, setter: Method?): Boolean {
            try {
                if (setter != null) {
                    try {
                        setter.invoke(target, value)
                        return true
                    } catch (ignore: InvocationTargetException) {
                    } catch (ignore: IllegalAccessException) {
                    }

                }

                // Error calling the setter, just set the value
                field.set(target, value)
                return true
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return false
        }

        operator fun getValue(field: Field, target: Any, getter: Method?): Any? {

            try {
                if (getter != null) {
                    try {
                        return getter.invoke(target)
                    } catch (ignore: InvocationTargetException) {
                    } catch (ignore: IllegalAccessException) {
                    }

                }
                // No getter, just get the value
                return field.get(target)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }
    }
}
