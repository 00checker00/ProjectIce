package de.project.ice.inventory

import com.badlogic.gdx.utils.ObjectMap

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

    init {
        addCombination("Schnur", "Hook", ItemCombinator("SchnurHook"))
        addCombination("SchnurHook", "Stick", ItemCombinator("AngelAngel"))
    }
}
