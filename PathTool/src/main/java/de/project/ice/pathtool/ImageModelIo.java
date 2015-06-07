package de.project.ice.pathtool;

import com.badlogic.gdx.utils.JsonWriter;
import de.project.ice.pathlib.Shape;
import de.project.ice.utils.io.FilenameHelper;
import com.badlogic.gdx.math.Vector2;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.utils.Array;
import org.apache.commons.io.FileUtils;

/**
 * @author Aurelien Ribon | http://www.aurelienribon.com/
 */
public class ImageModelIo {
    public static void save(File file, List<ImageModel> models) throws IOException {
        String output = "";

        for (int i = 0; i < models.size(); i++) {
            if (i > 0) output += "\n\n---\n\n";

            output += "i " + FilenameHelper.relativize(models.get(i).file.getPath(), file.getParent());
            for (Shape shape : models.get(i).shapes) {
                output += "\ns ";
                Array<Vector2> vs = shape.vertices;
                for (Vector2 v : vs) output += (v == vs.get(0) ? "" : ",") + v.x + "," + v.y;
            }
        }

        FileUtils.writeStringToFile(file, output);
    }

    public static void export(File file, ImageModel model) throws IOException {
        JsonWriter json = new JsonWriter(new FileWriter(file));
        json.object().name("area").array();
        for (Shape shape : model.shapes) {
            json.object().name("shape").array();
            for (Vector2 v : shape.vertices) {
                json.object();
                json.set("x", v.x);
                json.set("y", v.y);
                json.pop();
            }
            json.pop();
            json.pop();
        }
        json.close();
    }

    public static List<ImageModel> load(File file) throws IOException {
        String input = FileUtils.readFileToString(file);
        List<ImageModel> models = new ArrayList<ImageModel>();

        String[] descriptions = input.split("---");

        for (String descrption : descriptions) {
            Array<String> lines = new Array<String>(descrption.trim().split("\n"));

            String path = findBlock(lines, "i");
            ImageModel model = new ImageModel(new File(file.getParent(), path));

            String vs;
            while (!(vs = findBlock(lines, "s")).equals("")) {
                Shape shape = new Shape();
                shape.vertices.addAll(parseVertices(vs));
                shape.closed = true;
                if (shape.vertices.size >= 3) model.shapes.add(shape);
            }

            model.lineOfSight();
            models.add(model);
        }

        return models;
    }

    private static String findBlock(Array<String> lines, String start) {
        for (int i = 0; i < lines.size; i++) {
            String line = lines.get(i);
            if (line.startsWith(start)) {
                lines.removeIndex(i);
                return line.substring(start.length()).trim();
            }
        }
        return "";
    }

    private static Array<Vector2> parseVertices(String input) {
        Array<Vector2> vs = new Array<Vector2>();
        String[] words = input.split(",");
        for (int i = 1; i < words.length; i += 2) {
            float x = Float.parseFloat(words[i - 1]);
            float y = Float.parseFloat(words[i]);
            vs.add(new Vector2(x, y));
        }
        return vs;
    }
}
