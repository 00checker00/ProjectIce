package de.project.ice.pathtool;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.ai.pfa.indexed.IndexedNode;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class PathCalculator {
    private static final float epsilon = 0.5f;

    private static boolean LineSegmentCrosses(Vector2 start, Vector2 end, Array<Vector2> vertices) {
        for (int i = 0; i < vertices.size; i++)
            if (LineSegmentsCross(start, end, vertices.get(i), vertices.get((i + 1) % vertices.size)))
                return true;
        return false;
    }

    private static boolean Inside(PathArea pathArea, Vector2 position) {
        if (!Inside(pathArea.shape, position, true))
            return false;
        return true;
    }

    public static boolean Inside(ImageModel.Shape shape, Vector2 position, boolean isOuterShape) {
        Vector2 point = position;

        boolean inside = false;

        Array<Vector2> vertices = shape.vertices;

        // Must have 3 or more edges
        if (vertices.size < 3) return false;

        Vector2 oldPoint = vertices.get(vertices.size - 1);
        float oldSqDist = oldPoint.dst2(point);

        for (int i = 0; i < vertices.size; i++) {
            Vector2 newPoint = vertices.get(i);
            float newSqDist = newPoint.dst2(point);

            if (oldSqDist + newSqDist + 2.0f * Math.sqrt(oldSqDist * newSqDist) - newPoint.dst2(oldPoint) < epsilon)
                return isOuterShape;

            Vector2 left;
            Vector2 right;
            if (newPoint.x > oldPoint.x) {
                left = oldPoint;
                right = newPoint;
            } else {
                left = newPoint;
                right = oldPoint;
            }

            if (left.x < point.x && point.x <= right.x && (point.y - left.y) * (right.x - left.x) < (right.y - left.y) * (point.x - left.x))
                inside = !inside;

            oldPoint = newPoint;
            oldSqDist = newSqDist;
        }

        return inside;
    }

    private static boolean InLineOfSight(PathArea pathArea, Vector2 start, Vector2 end) {
        // Not in LOS if any of the ends is outside the pathArea
        if (!Inside(pathArea, start) || !Inside(pathArea, end)) return false;

        // In LOS if it's the same start and end location
        if (start.dst(end) < epsilon) return true;

        Vector2 mid = new Vector2(start).sub(end).scl(0.5f).add(end);

        for (ImageModel.Shape hole : pathArea.holes) {
            if (Inside(hole, mid, false)) {
                return false;
            }
        }

        // Finally the middle point in the segment determines if in LOS or not
        return Inside(pathArea, mid);
    }

    private static boolean LineSegmentsCross(Vector2 a, Vector2 b, Vector2 c, Vector2 d) {
        float denominator = ((b.x - a.x) * (d.y - c.y)) - ((b.y - a.y) * (d.x - c.x));

        if (denominator == 0) {
            return false;
        }

        float numerator1 = ((a.y - c.y) * (d.x - c.x)) - ((a.x - c.x) * (d.y - c.y));
        float numerator2 = ((a.y - c.y) * (b.x - a.x)) - ((a.x - c.x) * (b.y - a.y));

        if (numerator1 == 0 || numerator2 == 0) {
            return false;
        }

        float r = numerator1 / denominator;
        float s = numerator2 / denominator;

        return (r > 0 && r < 1) && (s > 0 && s < 1);
    }

    public PathGraph computeGraph(PathArea pathArea) {
        PathGraph graph = new PathGraph();

        for (Vector2 vertex : pathArea.shape.vertices)
            graph.addNode(new PathNode(vertex));

        for (ImageModel.Shape hole : pathArea.holes)
            for (Vector2 vertex : hole.vertices)
                graph.addNode(new PathNode(vertex));

        for (PathNode waypoint : pathArea.waypoints)
            graph.addNode(waypoint);

        for (int n = 0; n < graph.nodes.size; n++) {
            PathNode startNode = graph.nodes.get(n);
            for (int m = 0; m < graph.nodes.size; m++) {
                PathNode endNode = graph.nodes.get(m);

                if (startNode == endNode)
                    continue;

                boolean lineOfSight = true;

                if (LineSegmentCrosses(startNode.pos, endNode.pos, pathArea.shape.vertices))
                    lineOfSight = false;

                for (ImageModel.Shape hole : pathArea.holes) {
                    if (LineSegmentCrosses(startNode.pos, endNode.pos, hole.vertices)) {
                        lineOfSight = false;
                        break;
                    }
                }

                if (lineOfSight && InLineOfSight(pathArea, startNode.pos, endNode.pos)) {
                    startNode.connections.add(new PathConnection(startNode, endNode));
                }
            }
        }
        return graph;
    }

    public static class PathArea {
        public final Array<ImageModel.Shape> holes = new Array<ImageModel.Shape>();
        public final Array<PathNode> waypoints = new Array<PathNode>();
        public ImageModel.Shape shape;
    }

    public static class PathGraph implements IndexedGraph<PathNode> {
        private final Array<PathNode> nodes = new Array<PathNode>();

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

    public static class PathConnection implements Connection<PathNode> {
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

    public static class PathNode implements IndexedNode<PathNode> {
        private final Array<Connection<PathNode>> connections = new Array<Connection<PathNode>>();
        private int index = -1;
        private Vector2 pos;

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

    public static class PathHeuristic implements Heuristic<PathNode> {

        @Override
        public float estimate(PathNode startNode, PathNode endNode) {
            return startNode.pos.dst(endNode.pos);
        }
    }
}
