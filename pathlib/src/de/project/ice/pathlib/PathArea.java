package de.project.ice.pathlib;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;


public class PathArea {
    @NotNull
    public final Array<Shape> holes = new Array<Shape>();
    @NotNull
    public final Array<PathNode> waypoints = new Array<PathNode>();
    @Nullable
    public Shape shape = null;

    @NotNull
    public static PathArea load(InputStream stream) {
        PathArea area = new PathArea();

        JsonReader reader = new JsonReader();
        JsonValue root = reader.parse(stream);
        for (JsonValue shape : root) {
            Shape s = new Shape();
            s.closed = true;
            for (JsonValue vertex : shape) {
                float x = vertex.getFloat("x");
                float y = vertex.getFloat("y");
                s.vertices.add(new Vector2(x, y));
            }
            if (area.shape == null) {
                area.shape = s;
            } else {
                area.holes.add(s);
            }
        }
        return area;
    }
}
