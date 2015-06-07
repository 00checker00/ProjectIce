package de.project.ice.pathlib;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedNode;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class PathNode implements IndexedNode<PathNode> {
    final Array<Connection<PathNode>> connections = new Array<Connection<PathNode>>();
    int index = -1;
    Vector2 pos;

    public PathNode(Vector2 pos) {
        this.pos = pos;
    }

    public Vector2 getPos() {
        return pos;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public Array<Connection<PathNode>> getConnections() {
        return connections;
    }
}
