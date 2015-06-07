package de.project.ice.dialog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class Branch {
    @NotNull
    public final String variable_name;
    @NotNull
    public final HashMap<String, Node> connections = new HashMap<String, Node>(5);

    public Branch(@NotNull String variable_name) {
        this.variable_name = variable_name;
    }

    public boolean hasDefault() {
        return connections.containsKey("__DEFAULT__");
    }

    @Nullable
    public Node getDefault() {
        return connections.get("__DEFAULT__");
    }

    @Nullable
    public Node getForValue(String value) {
        if (connections.containsKey(value))
            return connections.get(value);
        else
            return getDefault();
    }
}
