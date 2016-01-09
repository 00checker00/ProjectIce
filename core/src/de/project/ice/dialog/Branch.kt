package de.project.ice.dialog

import java.util.*

class Branch(val variable_name: String) {
    val connections = HashMap<String, Node>(5)

    fun hasDefault(): Boolean {
        return connections.containsKey("__DEFAULT__")
    }

    val default: Node?
        get() = connections["__DEFAULT__"]

    fun getForValue(value: String): Node? {
        if (connections.containsKey(value)) {
            return connections[value]
        } else {
            return default
        }
    }
}
