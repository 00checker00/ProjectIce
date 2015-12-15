package de.project.ice.inventory;

import com.badlogic.gdx.utils.Array;
import de.project.ice.IceGame;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Inventory
{
    @NotNull
    public final Array<Item> items = new Array<Item>();
    @NotNull
    private final IceGame game;

    public Inventory(@NotNull IceGame game)
    {
        this.game = game;
    }

    public void addItem(String name)
    {
        removeItem(name);
        Item item = Items.get(name);
        item.inventory = this;
        items.add(item);
    }

    public void removeItem(String name)
    {
        items.removeValue(Items.get(name), true);
    }

    public static final class Item 
    {
        private Inventory inventory = null;
        @NotNull
        public final String name;
        @NotNull
        public final String icon;
        @Nullable
        public final String description;
        @Nullable
        public final ItemListener listener;

        public Item()
        {
            this("INVALID", "invalid_item", "Invalid Item");
        }

        protected Inventory Inventory() 
        {
            return inventory;
        }

        public Item(@NotNull String name, @NotNull String icon, @Nullable String description, @Nullable ItemListener listener)
        {
            this.name = name;
            this.icon = icon;
            this.description = description;
            this.listener = listener;
        }

        public Item(@NotNull String name, @NotNull String icon, @Nullable String description)
        {
            this(name, icon, description, null);
        }
    }

    public interface ItemListener
    {
        void itemClicked();
    }
}
