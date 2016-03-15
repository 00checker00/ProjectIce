package de.project.ice.inventory

import com.badlogic.gdx.utils.ObjectMap
import de.project.ice.IceGame

object Combinations {
    private val combinations = ObjectMap<String, Combinator>()

    private fun addCombination(item1: String, item2: String, combinator: Combinator) {
        combinations.put(item1 + ":" + item2, combinator)
        combinations.put(item2 + ":" + item1, combinator)
    }


    interface Combinator {
        fun combine(inventory: Inventory, item1: String, item2: String)
    }

    private class ItemCombinator(private val newItem: String) : Combinator {


        override fun combine(inventory: Inventory, item1: String, item2: String) {
            inventory.removeItem(item1)
            inventory.removeItem(item2)
            inventory.addItem(newItem)
        }
    }

    public fun canCombine(item1: Inventory.Item, item2: Inventory.Item): Boolean {
        return combinations.containsKey("${item1.name}:${item2.name}")
    }

    public fun combine(item1: Inventory.Item, item2: Inventory.Item) {
        combinations.get("${item1.name}:${item2.name}", null)?.combine(item1.inventory!!, item1.name, item2.name);
    }

    init {
        addCombination("inv_fishing_rod", "inv_worm", ItemCombinator("inv_fishing_rod_worm"))
       // addCombination("SchnurHook", "Stick", ItemCombinator("AngelAngel"))




    }
}
