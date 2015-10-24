package de.project.ice.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

public abstract class Assets {

    public static AssetManager manager = new AssetManager();
    private static TextureAtlas charsSheet = null;
    private static TextureAtlas inventorySheet = null;
    private static TextureAtlas sceneSheet = null;
    private static String currentScene = null;
    private static HashMap<String, Array<TextureRegion>> cachedRegionsChars = new HashMap<String, Array<TextureRegion>>();
    private static HashMap<String, Array<TextureRegion>> cachedRegionsInventory = new HashMap<String, Array<TextureRegion>>();
    private static HashMap<String, Array<TextureRegion>> cachedRegionsScene = new HashMap<String, Array<TextureRegion>>();

    static {
    }

    public static boolean loadScene(String scene) {
        scene = "spritesheets/" + scene + ".atlas";

        if (scene.equals(currentScene)) return true;

        if(currentScene != null) {
            manager.clear();
            cachedRegionsInventory.clear();
            cachedRegionsScene.clear();
            cachedRegionsChars.clear();
            charsSheet = null;
            sceneSheet = null;
            inventorySheet = null;
        }

        if (charsSheet == null) {
            manager.load("spritesheets/objects.atlas", TextureAtlas.class);
            manager.load("spritesheets/inventory.atlas", TextureAtlas.class);
        }

        if (Gdx.files.internal(scene).exists()) {
            currentScene = scene;
            manager.load(currentScene, TextureAtlas.class);
            return true;
        } else {
            currentScene = null;
            sceneSheet = null;
            return false;
        }
    }

    public static TextureRegionsHolder findRegions(String name) {
        if(charsSheet == null) {
            manager.finishLoading();
            charsSheet = manager.get("spritesheets/objects.atlas", TextureAtlas.class);
        }
        if (inventorySheet == null) {
            manager.finishLoading();
            inventorySheet = manager.get("spritesheets/inventory.atlas", TextureAtlas.class);
        }
        if(sceneSheet == null && currentScene != null) {
            manager.finishLoading();
            sceneSheet = manager.get(currentScene, TextureAtlas.class);
        }

        if(cachedRegionsChars.containsKey(name)) {
            return new TextureRegionsHolder(cachedRegionsChars.get(name), name);
        }

        if (cachedRegionsInventory.containsKey(name)) {
            return new TextureRegionsHolder(cachedRegionsInventory.get(name), name);
        }
        if(cachedRegionsScene.containsKey(name)) {
            return new TextureRegionsHolder(cachedRegionsScene.get(name), name);
        }

        Array<TextureRegion> regions = new Array<TextureRegion>();
        regions.addAll(charsSheet.findRegions(name));

        if (regions.size == 0 && sceneSheet != null) {
            regions.addAll(inventorySheet.findRegions(name));
            if (regions.size > 0) {
                cachedRegionsInventory.put(name, regions);
            } else {
                regions.addAll(sceneSheet.findRegions(name));
                if (regions.size > 0)
                    cachedRegionsScene.put(name, regions);
            }
        } else {
            cachedRegionsChars.put(name, regions);
        }

        return new TextureRegionsHolder(regions, name);
    }

    public static TextureRegionHolder findRegion(String name) {
        Holder<Array<TextureRegion>> regions = findRegions(name);
        if(regions.data.size == 0)
            return new TextureRegionHolder(name);
        else
            return new TextureRegionHolder(regions.data.first(), name);
    }

    public static AnimationHolder createAnimation(String name, float frameDuration, Animation.PlayMode playMode) {
        Holder<Array<TextureRegion>> regions = findRegions(name);
        if (!regions.isValid())
            return new AnimationHolder(name);
        Animation animation = new Animation(frameDuration, regions.data, playMode);
        return new AnimationHolder(animation, name);
    }

    public abstract static class Holder<T> {
        public T data;
        public String name;

        public Holder(T data, String name) {
            this.data = data;
            this.name = name;
        }

        public Holder(String name) {
            this(null, name);
        }

        public Holder() {
            this(null, "null");
        }

        public boolean isValid() {
            return data != null;
        }
    }

    public static final class TextureRegionHolder extends Holder<TextureRegion> {
        public TextureRegionHolder(TextureRegion data, String name) {
            super(data, name);
        }

        public TextureRegionHolder(String name) {
            super(name);
        }

        public TextureRegionHolder() {
        }
    }

    public static final class TextureRegionsHolder extends Holder<Array<TextureRegion>> {
        public TextureRegionsHolder(Array<TextureRegion> data, String name) {
            super(data, name);
        }

        public TextureRegionsHolder(String name) {
            super(name);
        }

        public TextureRegionsHolder() {
        }
    }

    public static final class AnimationHolder extends Holder<Animation> {
        public AnimationHolder(Animation data, String name) {
            super(data, name);
        }

        public AnimationHolder(String name) {
            super(name);
        }

        public AnimationHolder() {
        }
    }

    public static void update() {
        manager.update();
    }
}