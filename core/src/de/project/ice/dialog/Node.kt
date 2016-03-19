package de.project.ice.dialog

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.Array

class Node(val id: String) {
    enum class Type {
        Start, Node, Branch, Choice, Text, Set
    }

    var type = Type.Node
    var next: Node? = null
    var variable_name: String? = null
    var variable_value: String? = null
    var text = ""
    var speaker = ""
    var color = Color.BLACK;
    val choices = Array<Pair<Node, Int>>(0)
    var branch: Branch? = null

}
