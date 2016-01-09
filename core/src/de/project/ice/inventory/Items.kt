package de.project.ice.inventory

import com.badlogic.gdx.utils.ObjectMap

import de.project.ice.inventory.Inventory.Item

object Items {
    private val items = ObjectMap<String, Item>()
    private val itemNames: Array<String>? = null

    operator fun get(name: String): Item {
        var item: Item? = items.get(name)
        if (item == null) {
            item = Item()
        }

        return item
    }

    private fun addItem(item: Item) {
        items.put(item.name, item)
    }

    fun loadAllItems() {
        addItem(Item("AngelAngel", "angel_angel", "s2_angelangel_desc", null))
        addItem(Item("Hook", "haken_angel", "s2_hook_desc", null))
        addItem(Item("Schnur", "schnur_angel", "s2_schnur_desc", null))
        addItem(Item("SchnurHook", "hakenschnur_angel", "s2_schnurhook_desc", null))
        addItem(Item("Stick", "stock_angel", "s2_stock_desc", null))
        addItem(Item("Teapot", "teekanne", "s3_teapot_desc", null))
        addItem(Item("Wood", "holz", "s3_wood_desc", null))
    }

    init {
        // TODO: background loading
        loadAllItems()
    }
}
