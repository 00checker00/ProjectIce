package de.project.ice.utils;


import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import de.project.ice.ecs.IceEngine;
import de.project.ice.ecs.systems.RenderingSystem;
import de.project.ice.pathlib.PathArea;
import de.project.ice.pathlib.Shape;
import de.project.ice.scripting.Script;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import static com.badlogic.gdx.utils.XmlReader.Element;

public abstract class SceneLoader {
    public static void loadScene(@NotNull IceEngine engine, @NotNull InputStream in) throws IOException, LoadException {
        loadScene(engine, new XmlReader().parse(in));
    }

    public static void loadScene(@NotNull IceEngine engine, @NotNull String in) throws IOException, LoadException {
        loadScene(engine, new XmlReader().parse(in));
    }

    public static void loadScene(@NotNull IceEngine engine, @NotNull Reader in) throws IOException, LoadException {
        loadScene(engine, new XmlReader().parse(in));
    }

    public static void loadScene(@NotNull IceEngine engine, @NotNull FileHandle in) throws IOException, LoadException {
        loadScene(engine, new XmlReader().parse(in));
    }



    private static void loadScene(@NotNull IceEngine engine, @Nullable Element scene) throws LoadException {
        if (scene == null || !scene.getName().equals("scene"))
            throw new LoadException("Invalid scene file (Not a scene file)");

        String sceneName = null;
        try {
            sceneName = scene.getAttribute("name");
            Assets.loadScene(sceneName);
        } catch (Exception ignore) {}

        Element entities = scene.getChildByName("entities");
        if (entities == null) {
            throw new LoadException("Invalid scene file (No entities entry)");
        }

        for (int i = 0; i < entities.getChildCount(); ++i) {
            Element child = entities.getChild(i);
            if (child.getName().endsWith("entity"))
                engine.addEntity(loadEntity(engine, child));
        }


        Element sounds = scene.getChildByName("sounds");

        if(sounds!=null) {

            for (int i = 0; i < sounds.getChildCount(); ++i) {
                Element child = sounds.getChild(i);
                if (child.getName().equals("sound"))
                    engine.soundSystem.loadSound(child.getText());
            }
        }

        Element music = scene.getChildByName("music");
        if(music != null){
            engine.soundSystem.playMusic(music.getText(),true);
        }


        if (sceneName != null) {
            Script sceneScript = Script.loadScript( sceneName + "_load", engine.game);
            if (sceneScript != null) {
                sceneScript.onLoad();
            }
        }
    }

    @NotNull
    private static Entity loadEntity(@NotNull IceEngine engine,@NotNull Element element) throws LoadException {
        Element components = element.getChildByName("components");
        if (components == null)
            throw new LoadException("Invalid scene file (Missing components entry for entity)");

        Entity entity = engine.createEntity();

        for (int i = 0; i < components.getChildCount(); ++i) {
            entity.add(loadComponent(engine, components.getChild(i)));
        }

        return entity;
    }

    @NotNull
    @SuppressWarnings("unchecked")
    private static Component loadComponent(@NotNull IceEngine engine, @NotNull Element element) throws LoadException {
        Class<? extends Component> componentClass;
        try {
            componentClass = ClassReflection.forName("de.project.ice.ecs.components." + element.getName());
        } catch (ReflectionException e) {
            throw new LoadException("Invalid scene file (Unknown Component: " + element.getName() + ")", e);
        }

        Component component = engine.createComponent(componentClass);
        if (component == null)
            throw new LoadException("Invalid scene file (Error creating component: " + element.getName() + ")");

        for (int i = 0; i < element.getChildCount(); ++i) {
            Element childElement = element.getChild(i);
            String type = childElement.getAttribute("type", "null");
            if (type.equalsIgnoreCase("null"))
                continue;

            Field f;
            try {
                f = ClassReflection.getField(componentClass, childElement.getName());
            } catch (ReflectionException e) {
                throw new LoadException("Invalid scene file (Component " + element.getName() + " does not have a " + childElement.getName()+ " field)", e);
            }

            try {
                f.set(component, loadValue(type, childElement));
            } catch (ReflectionException e) {
                throw new LoadException("Invalid scene file (Couldn't set field " + childElement.getName() + " of component " + element.getName() + ")", e);
            }
        }
        return component;
    }

    @NotNull
    private static Object loadValue(@NotNull String type, @NotNull Element element) throws LoadException {
        if (type.equalsIgnoreCase("Float")) {
            return loadFloat(element);
        } else if (type.equalsIgnoreCase("Integer")) {
            return loadInt(element);
        } else if (type.equalsIgnoreCase("Boolean")) {
            return loadBoolean(element);
        } else if (type.equalsIgnoreCase("String")) {
            return element.getText() == null ? "" : element.getText().replace("&gt", ">").replace("&lt", "<").replace("&amp", "&");
        } else if (type.equalsIgnoreCase("OrthographicCamera")) {
            return loadOrthographicCamera(element);
        } else if (type.equalsIgnoreCase("Vector2")) {
            return loadVector2(element);
        } else if (type.equalsIgnoreCase("Vector3")) {
            return loadVector3(element);
        } else if (type.equalsIgnoreCase("IntMap")) {
            return loadIntMap(element);
        } else if (type.equalsIgnoreCase("TextureRegionHolder")) {
            return loadTextureRegion(element);
        } else if (type.equalsIgnoreCase("AnimationHolder")) {
            return loadAnimation(element);
        } else if (type.equalsIgnoreCase("PathArea")) {
            return loadPathArea(element);
        } else
            throw new LoadException("Invalid scene file (Unknown Component type: " + type + ")");
    }

    private static Boolean loadBoolean (Element element) {
        return element.getText().trim().equals("true");
    }

    private static PathArea loadPathArea (@NotNull Element element) {
        PathArea pathArea = new PathArea();
        Element shapeelement = element.getChildByName("Shape");
        pathArea.shape = loadShape(element.getChildByName("shape"));

        Element holes = element.getChildByName("holes");
        for (int i = 0; i < holes.getChildCount(); ++i) {
            pathArea.holes.add(loadShape(holes.getChild(i)));
        }
        return pathArea;
    }


    private static Shape loadShape (@NotNull Element element) {
        Shape shape = new Shape();
        Element vertices = element.getChildByName("vertices");
        for (int i = 0; i < vertices.getChildCount(); ++i) {
            shape.vertices.add(loadVector2(vertices.getChild(i)));
        }

        shape.closed = element.getBoolean("closed");

        return shape;
    }

    private static float loadFloat(@NotNull Element element) {
        return Float.parseFloat(element.getText());
    }


    private static int loadInt(@NotNull Element element) {
        return Integer.parseInt(element.getText());
    }

    @NotNull
    private static Assets.TextureRegionHolder loadTextureRegion(@NotNull Element element) {
        return Assets.findRegion(element.getText());
    }

    @NotNull
    private static Assets.AnimationHolder loadAnimation(@NotNull Element element) {
        return Assets.createAnimation(element.getText(), element.getFloatAttribute("frameDuration", 0f), Animation.PlayMode.valueOf(element.getAttribute("mode", "NORMAL")));
    }

    @NotNull
    @SuppressWarnings("unchecked")
    private static IntMap loadIntMap(@NotNull Element element) throws LoadException {
        String subType = element.getAttribute("subType", "null");

        IntMap map = new IntMap();

        if ("null".equalsIgnoreCase(subType))
            return map;

        for (int i = 0; i < element.getChildCount(); ++i) {
            Element childElement = element.getChild(i);
            int key = Integer.parseInt(childElement.getName().substring(1));
            Object value = loadValue(subType, childElement);
            map.put(key, value);
        }

        return map;
    }

    @NotNull
    private static OrthographicCamera loadOrthographicCamera(@NotNull Element element) throws LoadException {
        Element posElement = element.getChildByName("pos");
        Vector3 pos;
        if (posElement == null)
            pos = new Vector3(0f, 0f, 0f);
        else
            pos = loadVector3(posElement);

        OrthographicCamera camera = new OrthographicCamera(RenderingSystem.FRUSTUM_WIDTH, RenderingSystem.FRUSTUM_HEIGHT);
        camera.position.set(pos);
        camera.viewportWidth = element.getFloatAttribute("viewportWidth", RenderingSystem.FRUSTUM_WIDTH);
        camera.viewportHeight = element.getFloatAttribute("viewportHeight", RenderingSystem.FRUSTUM_HEIGHT);
        return camera;
    }

    @NotNull
    private static Vector3 loadVector3(@NotNull Element element) {
        return new Vector3(element.getFloatAttribute("x", 0f), element.getFloatAttribute("y", 0f), element.getFloatAttribute("z", 0f));
    }

    @NotNull
    private static Vector2 loadVector2(@NotNull Element element) {
        return new Vector2(element.getFloatAttribute("x", 0f), element.getFloatAttribute("y", 0f));
    }

    public static class LoadException extends Exception {
        public LoadException() {
        }

        public LoadException(String message) {
            super(message);
        }

        public LoadException(String message, Throwable cause) {
            super(message, cause);
        }

        public LoadException(Throwable cause) {
            super(cause);
        }
    }
}
