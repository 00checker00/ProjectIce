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

        addItem(Item("inv_worm", "inv_worm", "inv_worm_desc", null))
        addItem(Item("inv_fishing_rod", "inv_fishing_rod", "inv_fishing_rod_desc", null))
        addItem(Item("inv_fishing_rod_nohook", "inv_fishing_rod_nohook", "inv_fishing_rod_nohook_desc", null))


        addItem(Item("inv_nick", "inv_nick", "inv_nick_desc", null))
        addItem(Item("inv_felicia", "inv_felicia", "inv_felicia_desc", null))
        addItem(Item("inv_shaft", "inv_shaft", "inv_shaft_desc", null))
        addItem(Item("inv_fishing_rod_worm", "inv_fishing_rod_worm", "inv_fishing_rod_worm_desc", null))
        addItem(Item("inv_hook", "inv_hook", "inv_hook_desc", null))

        addItem(Item("inv_note", "inv_note", "inv_note_desc", null))
        addItem(Item("inv_note_shred_1", "inv_note_shred_1", "inv_note_shred_1_desc", null))
        addItem(Item("inv_note_shred_2", "inv_note_shred_2", "inv_note_shred_2_desc", null))
        addItem(Item("inv_note_shred_3", "inv_note_shred_3", "inv_note_shred_3_desc", null))
        addItem(Item("inv_note_shred_4", "inv_note_shred_4", "inv_note_shred_4_desc", null))
        addItem(Item("inv_note_shred_1_2", "inv_note_shred_1_2", "inv_note_shred_1_2_desc", null))

    }

    init {
        // TODO: background loading
        loadAllItems()
    }
}
