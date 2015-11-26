package de.project.ice.dialog;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import de.project.ice.utils.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.HashMap;

public abstract class Dialog
{
    public static Node load(FileHandle file)
    {
        JsonValue root = new JsonReader().parse(file);

        HashMap<String, Node> nodes = new HashMap<String, Node>();
        Node startNode = null;

        for (JsonValue value : root)
        {
            String type = value.getString("type");
            String id = value.getString("id");
            String nextId = value.getString("next", "null");
            Node node = loadNode(id, nodes);
            node.next = loadNode(nextId, nodes);

            if ("Start".equals(type))
            {
                node.type = Node.Type.Start;
                startNode = node;
            }
            else if ("Node".equals(type))
            {
                node.type = Node.Type.Node;
                node.text = value.getString("name");
            }
            else if ("Choice".equals(type))
            {
                node.type = Node.Type.Choice;
                node.text = value.getString("name");
            }
            else if ("Text".equals(type))
            {
                node.type = Node.Type.Text;
                node.text = value.getString("name");
            }
            else if ("Set".equals(type))
            {
                node.type = Node.Type.Set;
                node.variable_name = value.getString("variable");
                node.variable_value = value.getString("value");
            }
            else if ("Branch".equals(type))
            {
                node.type = Node.Type.Branch;
                node.branch = new Branch(value.getString("variable"));
            }

            if (value.has("choices"))
            {
                for (JsonValue choice : value.get("choices"))
                {
                    node.choices.add(Pair.create(loadNode(choice.getString("id"), nodes), choice.getInt("index", 0)));
                    node.choices.sort(new Comparator<Pair<Node, Integer>>()
                    {
                        @Override
                        public int compare(Pair<Node, Integer> o1, Pair<Node, Integer> o2)
                        {
                            return o1.getSecond() - o2.getSecond();
                        }
                    });
                }
            }

            if (value.has("branches") && node.branch != null)
            {
                for (JsonValue jsonBranch : value.get("branches"))
                {
                    String expected_value = jsonBranch.name;
                    String branchId = jsonBranch.asString();
                    node.branch.connections.put(expected_value, loadNode(branchId, nodes));
                }
            }
        }
        return startNode;
    }

    @Nullable
    private static Node loadNode(@NotNull String id, @NotNull HashMap<String, Node> nodes)
    {
        if ("null".equals(id))
        {
            return null;
        }
        if (!nodes.containsKey(id))
        {
            Node node = new Node(id);
            nodes.put(id, node);
            return node;
        }
        else
        {
            return nodes.get(id);
        }
    }
}
