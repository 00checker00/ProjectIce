package de.project.ice.inventory;

import com.badlogic.gdx.utils.ObjectMap;
import org.jetbrains.annotations.NotNull;

public abstract class Combinations
{
    private static final ObjectMap<String, Combinator> combinations = new ObjectMap<String, Combinator>();

    private static void addCombination(@NotNull String item1, @NotNull String item2, @NotNull Combinator combinator)
    {
        combinations.put(item1 + ":" + item2, combinator);
        combinations.put(item2 + ":" + item1, combinator);
    }


    public interface Combinator
    {
        void combine(Inventory inventory, String item1, String item2);
    }

    private static class ItemCombinator implements Combinator
    {
        private String newItem;

        public ItemCombinator(String newItem)
        {
            this.newItem = newItem;
        }


        @Override
        public void combine(Inventory inventory, String item1, String item2)
        {
            inventory.removeItem(item1);
            inventory.removeItem(item2);
            inventory.addItem(newItem);
        }
    }

    static
    {
        addCombination("Schnur", "Hook", new ItemCombinator("SchnurHook"));
        addCombination("SchnurHook", "Stick", new ItemCombinator("AngelAngel"));
    }
}
