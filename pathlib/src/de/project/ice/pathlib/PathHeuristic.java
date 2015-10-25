package de.project.ice.pathlib;

import com.badlogic.gdx.ai.pfa.Heuristic;

public class PathHeuristic implements Heuristic<PathNode>
{

    @Override
    public float estimate(PathNode startNode, PathNode endNode)
    {
        return startNode.pos.dst(endNode.pos);
    }
}
