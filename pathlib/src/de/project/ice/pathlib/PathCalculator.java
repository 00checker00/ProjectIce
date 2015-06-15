package de.project.ice.pathlib;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class PathCalculator {
    private final float epsilon;

    public PathCalculator() {
        this(0.5f);
    }

    public PathCalculator(float epsilon) {
        this.epsilon = epsilon;
    }

    private boolean LineSegmentCrosses(Vector2 start, Vector2 end, Array<Vector2> vertices) {
        for (int i = 0; i < vertices.size; i++)
            if (LineSegmentsCross(start, end, vertices.get(i), vertices.get((i + 1) % vertices.size)))
                return true;
        return false;
    }

    public boolean IsInside(PathArea pathArea, Vector2 position) {
        if (!IsInside(pathArea.shape, position, true))
            return false;
        for (Shape hole : pathArea.holes)
            if (IsInside(hole, position, false))
                return false;

        return true;
    }

    private boolean IsInside(Shape shape, Vector2 position, boolean isOuterShape) {
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

    private boolean InLineOfSight(PathArea pathArea, Vector2 start, Vector2 end) {
        // Not in LOS if any of the ends is outside the pathArea
        if (!IsInside(pathArea, start) || !IsInside(pathArea, end)) return false;

        // In LOS if it's the same start and end location
        if (start.dst(end) < epsilon) return true;

        Vector2 mid = new Vector2(start).sub(end).scl(0.5f).add(end);

        // Not in LOS if the middle point is inside any hole
        for (Shape hole : pathArea.holes) {
            if (IsInside(hole, mid, false)) {
                return false;
            }
        }

        // Finally the middle point in the segment determines if in LOS or not
        return IsInside(pathArea, mid);
    }

    private boolean LineSegmentsCross (Vector2 start_1, Vector2 end_1, Vector2 start_2, Vector2 end_2) {
        float denominator = ((end_1.x - start_1.x) * (end_2.y - start_2.y)) - ((end_1.y - start_1.y) * (end_2.x - start_2.x));

        if (denominator == 0) {
            return false;
        }

        float numerator1 = ((start_1.y - start_2.y) * (end_2.x - start_2.x)) - ((start_1.x - start_2.x) * (end_2.y - start_2.y));
        float numerator2 = ((start_1.y - start_2.y) * (end_1.x - start_1.x)) - ((start_1.x - start_2.x) * (end_1.y - start_1.y));

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

        for (Shape hole : pathArea.holes)
            for (Vector2 vertex : hole.vertices)
                graph.addNode(new PathNode(vertex));

        for (PathNode waypoint : pathArea.waypoints) {
            if (!IsInside(pathArea, waypoint.pos))
                moveWaypointInside(graph, pathArea, waypoint);
            graph.addNode(waypoint);
        }

        for (int n = 0; n < graph.nodes.size; n++) {
            PathNode startNode = graph.nodes.get(n);
            for (int m = 0; m < graph.nodes.size; m++) {
                PathNode endNode = graph.nodes.get(m);

                if (startNode != endNode && ConnectionValid(startNode.pos, endNode.pos, pathArea)) {
                    startNode.connections.add(new PathConnection(startNode, endNode));
                }
            }
        }
        return graph;
    }

    public boolean ConnectionValid (Vector2 start, Vector2 end, PathArea area) {
        if (start == end)
            return false;

        boolean lineOfSight = true;

        if (LineSegmentCrosses(start, end, area.shape.vertices))
            lineOfSight = false;

        for (Shape hole : area.holes) {
            if (LineSegmentCrosses(start, end, hole.vertices)) {
                lineOfSight = false;
                break;
            }
        }
        return lineOfSight && InLineOfSight(area, start, end);
    }

    private void moveWaypointInside (PathGraph graph, PathArea area, PathNode waypoint) {
        Vector2 closest = waypoint.pos;
        float closestDistance = Float.MAX_VALUE;
        for (int i = 1; i <= area.shape.vertices.size; i++) {
            try {
                Vector2 closestOnSegment = closestPoint(
                        waypoint.pos,
                        area.shape.vertices.get(i - 1),
                        area.shape.vertices.get(i % area.shape.vertices.size)
                );
                float distanceToSegment = closestOnSegment.dst(waypoint.pos);
                if (distanceToSegment < closestDistance) {
                    closest = closestOnSegment;
                    closestDistance = distanceToSegment;
                }
            } catch (IllegalArgumentException ignored) {

            }
        }
        for (Shape hole : area.holes) {
            for (int i = 1; i <= hole.vertices.size; i++) {
                try {
                    Vector2 closestOnSegment = closestPoint(
                            waypoint.pos,
                            hole.vertices.get(i - 1),
                            hole.vertices.get(i % hole.vertices.size)
                    );
                    float distanceToSegment = closestOnSegment.dst(waypoint.pos);
                    if (distanceToSegment < closestDistance) {
                        closest = closestOnSegment;
                        closestDistance = distanceToSegment;
                    }
                } catch (IllegalArgumentException ignored) {

                }
            }
        }
        waypoint.pos.set(closest);
    }


    private Vector2 closestPoint (Vector2 point, Vector2 start, Vector2 end) throws IllegalArgumentException {
        final float dX = end.x - start.x;
        final float dY = end.y - start.y;

        if ((dX == 0) && (dY == 0)) {
            throw new IllegalArgumentException("start and end cannot be the same point");
        }

        final float u = ((point.x - start.x) * dX + (point.y - start.y) * dY) / (dX * dX + dY * dY);

        final Vector2 closestPoint;
        if (u < 0) {
            closestPoint = start;
        } else if (u > 1) {
            closestPoint = end;
        } else {
            closestPoint = new Vector2(start.x + u * dX, start.y + u * dY);
            closestPoint.add(new Vector2(closestPoint.x - point.x, closestPoint.y - point.y).nor().scl(epsilon));
        }

        return closestPoint;
    }

}
