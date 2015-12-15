package de.project.ice.inventory;

import com.badlogic.gdx.utils.ObjectMap;
import org.jetbrains.annotations.NotNull;

import static de.project.ice.inventory.Inventory.Item;

public abstract class Items
{
    private static final ObjectMap<String, Item> items = new ObjectMap<String, Item>();
    private static String[] itemNames;

    @NotNull
    public static Item get(String name)
    {
        Item item = items.get(name);
        if (item == null)
        {
            item = new Item();
        }

        return item;
    }

    private static void addItem(@NotNull Item item)
    {
        items.put(item.name, item);
    }

    public static void loadAllItems()
    {
        addItem(new Item("AngelAngel", "angel_angel", "s2_angelangel_desc", null));
        addItem(new Item("Hook", "haken_angel", "s2_hook_desc", null));
        addItem(new Item("Schnur", "schnur_angel", "s2_schnur_desc", null));
        addItem(new Item("SchnurHook", "hakenschnur_angel", "s2_schnurhook_desc", null));
        addItem(new Item("Stick", "stock_angel", "s2_stock_desc", null));
        addItem(new Item("Teapot", "teekanne", "s3_teapot_desc", null));
        addItem(new Item("Wood", "holz", "s3_wood_desc", null));
    }

    static
    {
        // TODO: background loading
        loadAllItems();
    }
}
