package de.project.ice.dialog

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.JsonReader
import java.util.*

object Dialog {
    fun load(file: FileHandle): Node {
        val root = JsonReader().parse(file)

        val nodes = HashMap<String, Node>()
        var startNode: Node? = null

        for (value in root) {
            val type = value.getString("type")
            val id = value.getString("id")
            val nextId = value.getString("next", "null")
            val node = loadNode(id, nodes)!!
            node.next = loadNode(nextId, nodes)

            if ("Start" == type) {
                node.type = Node.Type.Start
                startNode = node
            } else if ("Node" == type) {
                node.type = Node.Type.Node
                node.text = value.getString("name")
            } else if ("Choice" == type) {
                node.type = Node.Type.Choice
                node.text = value.getString("name")
            } else if ("Text" == type) {
                node.type = Node.Type.Text
                node.text = value.getString("name")
            } else if ("Set" == type) {
                node.type = Node.Type.Set
                node.variable_name = value.getString("variable")
                node.variable_value = value.getString("value")
            } else if ("Branch" == type) {
                node.type = Node.Type.Branch
                node.branch = Branch(value.getString("variable"))
            }

            if (value.has("choices")) {
                for (choice in value.get("choices")) {
                    node.choices.add(Pair(loadNode(choice.getString("id"), nodes)!!, choice.getInt("index", 0)))
                    node.choices.sort({ o1, o2 -> o1.second - o2.second })
                }
            }

            if (value.has("branches") && node.branch != null) {
                for (jsonBranch in value.get("branches")) {
                    val expected_value = jsonBranch.name
                    val branchId = jsonBranch.asString()
                    node.branch!!.connections.put(expected_value, loadNode(branchId, nodes)!!)
                }
            }
        }
        return startNode!!
    }

    private fun loadNode(id: String, nodes: HashMap<String, Node>): Node? {
        if ("null" == id) {
            return null
        }
        if (!nodes.containsKey(id)) {
            val node = Node(id)
            nodes.put(id, node)
            return node
        } else {
            return nodes[id]
        }
    }
}
