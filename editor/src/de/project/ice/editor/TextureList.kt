package de.project.ice.editor

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ObjectSet
import com.kotcrab.vis.ui.widget.VisList
import de.project.ice.utils.Assets

class TextureList : VisList<TextureAtlas.AtlasRegion>() {
    private val selectionChangedListeners = ObjectSet<SelectionChangedListener>()

    fun addListener(listener: SelectionChangedListener) {
        selectionChangedListeners.add(listener)
    }

    fun removeListener(listener: SelectionChangedListener) {
        selectionChangedListeners.remove(listener)
    }

    init {
        val set = ObjectSet<String>()
        val array = Array<TextureAtlas.AtlasRegion>()
        for (region in Assets.allRegions) {
            if (!set.contains(region.name)) {
                set.add(region.name)
                array.add(region)
            }
        }
        array.sort { o1, o2 -> o1.name.compareTo(o2.name) }
        setItems(array)

        addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                for (listener in selectionChangedListeners) {
                    listener.selectionChanged(selected, selectedIndex)
                }
                return super.touchDown(event, x, y, pointer, button)
            }
        })
    }

    interface SelectionChangedListener {
        fun selectionChanged(newSelection: TextureAtlas.AtlasRegion, newIndex: Int)
    }
}
