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
        fun combine(game: IceGame, item1: String, item2: String)
    }

    private open class ItemCombinator(private val newItem: String) : Combinator {


        override open fun combine(game: IceGame, item1: String, item2: String) {
            game.inventory.removeItem(item1)
            game.inventory.removeItem(item2)
            game.inventory.addItem(newItem)
        }
    }

    fun canCombine(item1: Inventory.Item, item2: Inventory.Item): Boolean {
        return combinations.containsKey("${item1.name}:${item2.name}")
    }

    fun combine(game: IceGame, item1: Inventory.Item, item2: Inventory.Item) {
        combinations.get("${item1.name}:${item2.name}", null)?.combine(game, item1.name, item2.name);
    }

    data class ItemPair(val item1: String, val item2: String)
    data class ItemPairWithResult(val pair: ItemPair, val result: String)

    infix fun String.and(other: String) = ItemPair(this, other)
    infix fun ItemPair.to(result: String) = ItemPairWithResult(this, result)


    class Builder() {
        fun combination(combination: ItemPairWithResult) {
            addCombination(combination.pair.item1, combination.pair.item2, ItemCombinator(combination.result))
        }
        fun combination(combination: ItemPair, func: IceGame.()->Unit) {
            addCombination(combination.item1, combination.item2, object: Combinator {
                override fun combine(game: IceGame, item1: String, item2: String) {
                    func.invoke(game)
                }
            })
        }
        fun combination(combination: ItemPairWithResult, func: IceGame.()->Unit) {
            addCombination(combination.pair.item1, combination.pair.item2, object: ItemCombinator(combination.result) {
                override fun combine(game: IceGame, item1: String, item2: String) {
                    super.combine(game, item1, item2)
                    func.invoke(game)
                }
            })
        }
    }

    fun buildCombinations(init: Builder.()->Unit): Unit {
        Builder().init()
    }


    init {
        buildCombinations {
            combination("inv_fishing_rod" and "inv_worm" to "inv_fishing_rod_worm")
        }
    }
}




