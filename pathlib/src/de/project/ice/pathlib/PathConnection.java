package de.project.ice.pathlib;

import com.badlogic.gdx.ai.pfa.Connection;


public class PathConnection implements Connection<PathNode> {
    private float cost = 0;
    private PathNode start = null;
    private PathNode end = null;

    public PathConnection(PathNode start, PathNode end) {
        this.start = start;
        this.end = end;
        this.cost = start.pos.dst(end.pos);
    }

    @Override
    public float getCost() {
        return cost;
    }

    @Override
    public PathNode getFromNode() {
        return start;
    }

    @Override
    public PathNode getToNode() {
        return end;
    }
}
