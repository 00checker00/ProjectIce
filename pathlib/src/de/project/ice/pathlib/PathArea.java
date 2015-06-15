package de.project.ice.pathlib;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static de.project.ice.config.Config.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;


public class PathArea {
    @NotNull
    public final Array<Shape> holes = new Array<Shape>();
    @NotNull
    public final Array<PathNode> waypoints = new Array<PathNode>();
    @Nullable
    public Shape shape = null;

    @NotNull
    public static PathArea load(String xml) {
        return load(new XmlReader().parse(xml));
    }

    @NotNull
    public static PathArea load(Reader reader) throws IOException {
        return load(new XmlReader().parse(reader));
    }

    @NotNull
    public static PathArea load(InputStream input) throws IOException {
        return load(new XmlReader().parse(input));
    }

    @NotNull
    public static PathArea load(FileHandle file) throws IOException {
        return load(new XmlReader().parse(file));
    }

    @NotNull
    private static PathArea load(XmlReader.Element root) {
        PathArea area = new PathArea();

        for (int i = 0; i < root.getChildCount(); ++i) {
            XmlReader.Element shape = root.getChild(i);
            Shape s = new Shape();
            s.closed = true;
            for (int j = 0; j < shape.getChildCount(); ++j) {
                XmlReader.Element vertex = shape.getChild(j);
                float x = vertex.getFloat("x") * PIXELS_TO_METRES;
                float y = vertex.getFloat("y") * PIXELS_TO_METRES;
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
