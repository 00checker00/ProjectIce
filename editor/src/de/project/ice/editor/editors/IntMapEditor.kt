package de.project.ice.editor.editors


import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.IntMap

import java.lang.reflect.Field

class IntMapEditor : BaseEditor() {
    private var field: Field? = null
    private var target: Any? = null
    private var map: IntMap<Any>? = null
    private val binders = Array<Binder>()
    private var description: String? = null

    override fun act(delta: Float) {
        super.act(delta)
        for (binder in binders) {
            if (binder.oldKey != binder.key) {
                var tmp: Binder? = null
                if (map!!.containsKey(binder.key)) {
                    for (i in 0..binders.size - 1) {
                        if (binders.get(i).oldKey == binder.key) {
                            tmp = binders.get(i)
                            break
                        }
                    }
                }
                map!!.remove(binder.oldKey)
                map!!.put(binder.key, binder.value)
                if (tmp != null) {
                    map!!.put(binder.oldKey, tmp.value)
                    tmp.key = binder.oldKey
                    tmp.oldKey = binder.oldKey
                }
                binder.oldKey = binder.key
                bind(field!!, target!!, "")
            }
            if (binder.value !== map!!.get(binder.key)) {
                map!!.put(binder.key, binder.value)
            }
        }
        if (map!!.size != binders.size) {
            bind(field!!, target!!, "")
        }
    }

    override fun clear() {
        super.clear()
        binders.clear()
    }

    override fun bind(field: Field, target: Any, description: String?): IntMapEditor {
        this.field = field
        this.target = target
        this.description = description
        clear()
        try {
            map = field.get(target) as IntMap<Any>
            for (entry in map!!) {
                val binder = Binder(entry.key, entry.value)
                add(NumberEditor.IntegerEditor().bind(Binder::class.java.getDeclaredField("key"), binder, "The " + entry.key + ". entry"))
                add(Editors.editorForClass(entry.value.javaClass).bind(Binder::class.java.getDeclaredField("value"), binder, description)).row()
                binders.add(binder)
            }
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }

        return this
    }

    inner class Binder(var key: Int, var value: Any) {
        var oldKey: Int = 0

        init {
            this.oldKey = key
        }
    }
}
