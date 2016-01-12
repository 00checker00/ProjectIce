package de.project.ice.editor.editors

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.kotcrab.vis.ui.widget.VisTextButton
import com.kotcrab.vis.ui.widget.VisTextField
import de.project.ice.utils.Assets

abstract class HolderEditor<T> : ValueEditor<Assets.Holder<T>>() {
    private var valueField: VisTextField? = null

    protected val holderName: String
        get() {
            if (value != null) {
                return value.name
            }
            return "invalid"
        }

    protected var holderData: T?
        get() {
            if (value != null) {
                return value.data
            }
            return null
        }
        set(data) {
            if (value != null) {
                val field = Assets.Holder::class.java.getDeclaredField("data")
                field.isAccessible = true
                field.set(value, data)
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
