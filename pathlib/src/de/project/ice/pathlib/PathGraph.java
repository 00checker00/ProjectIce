package de.project.ice.pathlib;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.utils.Array;

public class PathGraph implements IndexedGraph<PathNode> {
    final Array<PathNode> nodes = new Array<PathNode>();

    @Override
    public int getNodeCount() {
        return nodes.size;
    }

    public void addNode(PathNode node) {
        node.index = nodes.size;
        nodes.add(node);
    }

    public Array<PathNode> getNodes() {
        return nodes;
    }

    public Array<Connection<PathNode>> getConnections() {
        Array<Connection<PathNode>> allConnections = new Array<Connection<PathNode>>();
        for (PathNode node : nodes)
            allConnections.addAll(node.connections);
        return allConnections;
    }

    @Override
    public Array<Connection<PathNode>> getConnections(PathNode pathNode) {
        return pathNode.connections;
    }
}
