package de.project.ice.pathlib;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class PathNode
{
    final Array<Connection<PathNode>> connections = new Array<Connection<PathNode>>();
    int index = -1;
    Vector2 pos;

    public PathNode(Vector2 pos)
    {
        this.pos = pos;
    }

    public Vector2 getPos()
    {
        return pos;
    }

    public int getIndex()
    {
        return index;
    }

    public Array<Connection<PathNode>> getConnections()
    {
        return connections;
    }
}
