package de.project.ice.inventory;

/**
 * Created by Antoni on 29.06.2015.
 */
public abstract class Combine {
    protected static void combine(Hook hook, Schnur schnur) {
        hook.Inventory().addItem("SchnurHook");
        hook.Inventory().removeItem("Schnur");
        hook.Inventory().removeItem("Hook");
    }
}
