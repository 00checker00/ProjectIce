package de.project.ice.inventory;


import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import de.project.ice.IceGame;
import org.jetbrains.annotations.NotNull;

public class Inventory {
    @NotNull
    public final Array<Item> items = new Array<Item>();
    @NotNull
    private final IceGame game;

    public Inventory(@NotNull IceGame game) {
        this.game = game;
    }

    public void addItem(String name) {
        try {
            Class<Item> clazz = ClassReflection.forName("de.project.ice.inventory." + name);
            Item item = ClassReflection.newInstance(clazz);
            item.inventory = this;
            items.add(item);
        } catch (ReflectionException e) {
            e.printStackTrace();
        }
    }

    public static abstract class Item {
        private Inventory inventory = null;

        protected Inventory Inventory() {
            return inventory;
        }

        @NotNull
        public abstract String getIcon();

        public boolean canCombine(@NotNull Item other) {
            return false;
        }

        public void combine(@NotNull Item other) {

        }
    }
}
