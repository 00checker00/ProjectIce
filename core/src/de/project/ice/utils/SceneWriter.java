package de.project.ice.utils;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import de.project.ice.ecs.IceEngine;
import de.project.ice.ecs.systems.SoundSystem;
import de.project.ice.pathlib.PathArea;
import de.project.ice.pathlib.Shape;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public abstract class SceneWriter {
    public static void serializeScene(String sceneName, @NotNull IceEngine engine, @NotNull XmlWriter xml) throws IOException {
        xml.element("scene")
                .attribute("name", sceneName);


        serializeAudio(xml, engine.soundSystem);

        xml.element("entities");
        for (Entity entity : engine.getEntities()) {
            xml.element("entity");
            xml.element("components");
            for (Component component : entity.getComponents()) {
                xml.element(component.getClass().getSimpleName());
                for (Field f : ClassReflection.getFields(component.getClass())) {
                    if (f.isFinal())
                        continue;

                    xml.element(f.getName());
                    try {
                        serializeObject(xml, f.get(component));
                    } catch (ReflectionException e) {
                        e.printStackTrace();
                    }
                    xml.pop();
                }
                xml.pop();
            }
            xml.pop();
            xml.pop();
        }

        xml.pop();
        xml.pop();
        xml.close();
    }

    private static void serializeAudio(XmlWriter xml, SoundSystem sound) throws IOException {
        xml.element("music").text(sound.getMusic()).pop();

        xml.element("sounds");

        for(String s:sound.getSounds()){
            xml.element("sound").text(s).pop();
        }

        xml.pop();

    }

    private static void serializeObject(XmlWriter xml, Object o) throws IOException {
        if (o == null) return;
        Class type = o.getClass();
        if (Float.class.equals(type) || type == float.class ||
                Double.class.equals(type) || type == double.class ||
                Short.class.equals(type) || type == short.class ||
                Integer.class.equals(type) || type == int.class ||
                Long.class.equals(type) || type == long.class) {
            xml.attribute("type", type.getSimpleName());
            xml.text(o);
        } else if (String.class.equals(type)) {
            xml.attribute("type", type.getSimpleName());
            xml.text(((String) o).replace("&", "&amp").replace("<", "&lt").replace(">", "&gt"));
        } else if (Boolean.class.equals(type) || type == boolean.class) {
            xml.attribute("type", type.getSimpleName());
            Boolean bool = (Boolean) o;
            xml.write(bool ? "true" : "false");
        } else if (Vector2.class.equals(type)) {
            xml.attribute("type", type.getSimpleName());
            Vector2 vec = (Vector2) o;
            xml.attribute("x", vec.x);
            xml.attribute("y", vec.y);
        } else if (Vector3.class.equals(type)) {
            xml.attribute("type", type.getSimpleName());
            Vector3 vec = (Vector3) o;
            xml.attribute("x", vec.x);
            xml.attribute("y", vec.y);
            xml.attribute("z", vec.z);
        } else if (Assets.AnimationHolder.class.equals(type)) {
            xml.attribute("type", type.getSimpleName());
            Assets.AnimationHolder holder = (Assets.AnimationHolder) o;
            Animation animation = holder.data;
            xml.attribute("frameDuration", animation.getFrameDuration());
            xml.attribute("mode", animation.getPlayMode());
            xml.text(holder.name);
        } else if (Assets.TextureRegionHolder.class.equals(type)) {
            xml.attribute("type", type.getSimpleName());
            Assets.TextureRegionHolder holder = (Assets.TextureRegionHolder) o;
            xml.text(holder.name);
        } else if (IntMap.class.equals(type)) {
            xml.attribute("type", type.getSimpleName());
            for (IntMap.Entry<Object> entry : (IntMap<Object>) o) {
                if (entry.value != null) {
                    xml.attribute("subType", entry.value.getClass().getSimpleName());
                    break;
                }
            }
            for (IntMap.Entry<Object> entry : (IntMap<Object>) o) {
                xml.element("_" + entry.key);
                serializeObject(xml, entry.value);
                xml.pop();
            }
        } else if (OrthographicCamera.class.equals(type)) {
            xml.attribute("type", type.getSimpleName());
            OrthographicCamera cam = (OrthographicCamera) o;
            xml.attribute("viewportWidth", cam.viewportWidth);
            xml.attribute("viewportHeight", cam.viewportHeight);
            xml.element("pos");
            serializeObject(xml, cam.position);
            xml.pop();
        } else if (PathArea.class.equals(type)) {
            xml.attribute("type", type.getSimpleName());
            PathArea pathArea = (PathArea) o;

            xml.element("shape");
            serializeObject(xml, pathArea.shape);
            xml.pop();

            xml.element("holes");
            for (Shape hole : pathArea.holes) {
                xml.element("hole");
                serializeObject(xml, hole);
                xml.pop();
            }
            xml.pop();

        } else if (Shape.class.equals(type)) {
            xml.attribute("type", type.getSimpleName());
            Shape shape = (Shape) o;
            xml.attribute("closed", shape.closed);
            xml.element("vertices");
            for (Vector2 v : shape.vertices) {
                xml.element("vertex");
                serializeObject(xml, v);
                xml.pop();
            }
            xml.pop();
        } else {
            xml.attribute("type", "null");
        }
    }
}
