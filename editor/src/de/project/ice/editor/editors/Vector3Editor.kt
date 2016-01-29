package de.project.ice.editor.editors

import java.lang.reflect.Field

internal class Vector3Editor : BaseEditor() {
    override fun bind(field: Field, target: Any, description: String?): BaseEditor {
        try {
            val label = VisLabel("${field.name}:")
            if (description != null)
                Tooltip.Builder(description).target(label).build()
            add(label).row()
            val vector = field.get(target) as Vector3
            val x = Vector3::class.java.getField("x")
            add(NumberEditor.FloatEditor().bind(x, vector, "The x component")).padLeft(10f).row()
            val y = Vector3::class.java.getField("y")
            add(NumberEditor.FloatEditor().bind(y, vector, "The y component")).padLeft(10f).row()
            val z = Vector3::class.java.getField("z")
            add(NumberEditor.FloatEditor().bind(z, vector, "The z component")).padLeft(10f)
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        return this
    }
}
