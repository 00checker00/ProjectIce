package de.project.ice.inventory

import com.badlogic.gdx.utils.ObjectMap
import de.project.ice.IceGame

import de.project.ice.inventory.Inventory.Item
import de.project.ice.screens.InventoryScreen
import de.project.ice.screens.PapierstueckScreen

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

    class ItemBuilder() {
        private var name: String? = null
        private var icon: String? = null
        private var description: String? = null
        private var listener: Inventory.ItemListener? = null

        fun name(func: ()->String) { name = func.invoke() }
        fun icon(func: ()->String) { icon = func.invoke() }
        fun description(func: ()->String) { description = func.invoke() }
        fun onClick(func:(IceGame)->Unit) {
            listener = object: Inventory.ItemListener{
                override fun itemClicked(game: IceGame) {
                    func.invoke(game)
                }
            }
        }

        fun build(): Item = Item(
                name ?: throw NullPointerException("Item name not set!"),
                icon ?: name!!,
                description ?: name + "_desc",
                listener)
    }

    class Builder() {
        fun item(func: ItemBuilder.()->Unit) { addItem(ItemBuilder().apply { func() }.build()) }
    }

    private fun items(func: Builder.()->Unit) {
        Builder().func()
    }

    fun loadAllItems() {
        items {
            item { name { "inv_worm" } }
            item { name { "inv_fishing_rod" } }
            item { name { "inv_fishing_rod_nohook" } }


            item { name { "inv_nick" } }
            item { name { "inv_felicia" } }
            item { name { "inv_shaft" } }
            item { name { "inv_fishing_rod_worm" } }
            item { name { "inv_hook" } }

            item { name { "inv_note" } }
            item { name { "inv_note_shred_1" } }
            item { name { "inv_note_shred_2" } }
            item { name { "inv_note_shred_3" } }
            item { name { "inv_note_shred_4" }; onClick { it.addScreen(PapierstueckScreen(it)); it.removeScreen(InventoryScreen::class.java) } }
            item { name { "inv_note_shred_1_2" } }
        }
    }

    init {
        // TODO: background loading
        loadAllItems()
    }
}
