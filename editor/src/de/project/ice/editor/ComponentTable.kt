package de.project.ice.editor


import de.project.ice.annotations.Property
import de.project.ice.ecs.components.AnimationComponent
import de.project.ice.editor.editors.Editors
import de.project.ice.utils.Assets
import java.lang.reflect.Field

class ComponentTable(component: Component) : VisTable() {
    var component: Component
        protected set

    init {
        this.component = component
        defaults().align(Align.topLeft)
        createUi()
    }

    protected fun createUi() {
        addFieldsForObject(component)
        if (component is AnimationComponent) {
            row()
            val addAnimationButton = VisTextButton("Add animation", object : ChangeListener() {
                override fun changed(event: ChangeListener.ChangeEvent, actor: Actor) {
                    val animationComponent = component
                    var lastId = 0
                    for (entry in animationComponent.animations) {
                        lastId = entry.key
                    }
                    animationComponent.animations.put(++lastId, Assets.Holder.Animation())
                }
            })
            add(addAnimationButton).expandX().fill()
        }
    }

    private fun addFieldsForObject(o: Any) {


        val clazz = o.javaClass.kotlin
        for (property in clazz.memberProperties) {

            val f = property.javaField

            val propertyAnnotation = f.annotations.find { it is Property }



            if (propertyAnnotation != null && propertyAnnotation is Property && !propertyAnnotation.debug) {
                f.isAccessible = true
                addField(f, component, propertyAnnotation.Description)
                row()
            }
        }
    }

    private fun addField(f: Field, target: Any, description: String? = null) {
        val editor = Editors.editorForClass(f.type).bind(f, target, description)
        add(editor)
    }


}
