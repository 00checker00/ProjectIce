package de.project.ice.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;

public abstract class Assets {

    public static AssetManager manager = new AssetManager();
    private static TextureAtlas spritesheet = null;
    private static HashMap<String, Array<TextureRegion>> cachedRegions = new HashMap<String, Array<TextureRegion>>();

    static {
        manager.load("spritesheets/eksi2.atlas", TextureAtlas.class);
    }

    public static TextureRegionsHolder findRegions(String name) {
        if(spritesheet == null) {
            manager.finishLoading();
            spritesheet = manager.get("spritesheets/eksi2.atlas", TextureAtlas.class);
        }
        if(cachedRegions.containsKey(name)) {
            return new TextureRegionsHolder(cachedRegions.get(name), name);
        }

        Array<TextureRegion> regions = new Array<TextureRegion>();
        regions.addAll(spritesheet.findRegions(name));

        cachedRegions.put(name, regions);
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
        if (regions.isNull())
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

        public boolean isNull() {
            return data == null;
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
}