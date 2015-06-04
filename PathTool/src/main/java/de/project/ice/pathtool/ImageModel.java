package de.project.ice.pathtool;

import de.project.ice.utils.notifications.ChangeableObject;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * @author Aurelien Ribon | http://www.aurelienribon.com/
 */
public class ImageModel extends ChangeableObject {
    public final File file;
    public final List<Shape> shapes = new ArrayList<Shape>();
    public final Array<Connection<PathCalculator.PathNode>> linesOfSight = new Array<Connection<PathCalculator.PathNode>>();
    public final GraphPath<PathCalculator.PathNode> pathNodes = new DefaultGraphPath<PathCalculator.PathNode>();
    private final float w, h;
    public Vector2 mousePos = new Vector2();
    public Vector2 startPos = new Vector2();

    public ImageModel(File file) throws IOException {
        this.file = file.getCanonicalFile();

        BufferedImage img = ImageIO.read(file);
        w = img.getWidth();
        h = img.getHeight();
    }

    public void lineOfSight() {
        clearLineOfSight();
        PathCalculator pathCalc = new PathCalculator();

        if (shapes.size() == 0 || !shapes.get(0).closed)
            return;

        PathCalculator.PathArea pathArea = new PathCalculator.PathArea();
        pathArea.shape = shapes.get(0);

        for (int i = 1; i < shapes.size(); i++)
            pathArea.holes.add(shapes.get(i));

        PathCalculator.PathGraph pathGraph = pathCalc.computeGraph(pathArea);
        linesOfSight.addAll(pathGraph.getConnections());

    }

    public boolean findPath() {
        PathCalculator pathCalc = new PathCalculator();

        if (shapes.size() == 0 || !shapes.get(0).closed)
            return false;

        PathCalculator.PathArea pathArea = new PathCalculator.PathArea();
        pathArea.shape = shapes.get(0);

        for (int i = 1; i < shapes.size(); i++)
            pathArea.holes.add(shapes.get(i));

        PathCalculator.PathNode start = new PathCalculator.PathNode(startPos);
        pathArea.waypoints.add(start);

        PathCalculator.PathNode end = new PathCalculator.PathNode(mousePos);
        pathArea.waypoints.add(end);

        PathCalculator.PathGraph pathGraph = pathCalc.computeGraph(pathArea);
        linesOfSight.clear();
        linesOfSight.addAll(pathGraph.getConnections());

        IndexedAStarPathFinder<PathCalculator.PathNode> astar = new IndexedAStarPathFinder<PathCalculator.PathNode>(pathGraph);
        pathNodes.clear();
        return astar.searchNodePath(start, end, new PathCalculator.PathHeuristic(), pathNodes);
    }

    public void clear() {
        shapes.clear();
        clearLineOfSight();
    }

    public void clearLineOfSight() {
        linesOfSight.clear();
    }

    public static class Shape {
        public final Array<Vector2> vertices = new Array<Vector2>();
        public boolean closed;
    }
}
