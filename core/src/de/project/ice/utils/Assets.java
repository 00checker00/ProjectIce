package de.project.ice.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import org.jetbrains.annotations.NotNull;

public abstract class Assets
{
    @NotNull
    private static final TextureAtlas ATLAS_PLACEHOLDER = new TextureAtlas((TextureAtlas.TextureAtlasData)null);
    @NotNull
    public static AssetManager manager = new AssetManager();
    @NotNull
    private static ObjectMap<String, Array<TextureRegion>> cachedRegions = new ObjectMap<String, Array<TextureRegion>>();
    @NotNull
    private static ObjectMap<String, Pair<String, TextureAtlas>> loadedSpritesheets = new ObjectMap<String, Pair<String, TextureAtlas>>();

    public static boolean loadAtlas(String name)
    {
        if (loadedSpritesheets.containsKey(name))
        {
            return true;
        }

        FileHandle file = Gdx.files.internal("spritesheets/" + name + ".atlas");

        if (!file.exists())
        {
            Gdx.app.log(Assets.class.getSimpleName(), "Unable to load spritesheet \"" + file.path() + "\": File not found");
            return false;
        }

        manager.load(file.path(), TextureAtlas.class);

        loadedSpritesheets.put(name, Pair.create(name, ATLAS_PLACEHOLDER));


        System.out.println("Loaded assets: " + name);
        return true;
    }

//    public static boolean loadScene(String scene)
//    {
//        scene = "spritesheets/" + scene + ".atlas";
//
//        if (scene.equals(currentScene))
//        {
//            return true;
//        }
//
//        if (currentScene != null)
//        {
//            clear();
//        }
//
//        if (Gdx.files.internal(scene).exists())
//        {
//            currentScene = scene;
//            manager.load(currentScene, TextureAtlas.class);
//            return true;
//        }
//        else
//        {
//            currentScene = null;
//            sceneSheet = null;
//            return false;
//        }
//    }

    public static TextureRegionsHolder findRegions(String name)
    {
        // Look if region is already cached
        if (cachedRegions.containsKey(name))
        {
            return new TextureRegionsHolder(cachedRegions.get(name), name);
        }


        // The region isn't cached, so lets try to find it
        Array<TextureRegion> regions = new Array<TextureRegion>();
        for (Pair<String, TextureAtlas> pair: loadedSpritesheets.values())
        {
            // Check if the current atlas is loaded
            if (!manager.isLoaded("spritesheets/" + pair.getFirst() + ".atlas"))
            {
                // The atlas isn't loaded, so we block here
                manager.finishLoading();
            }

            //  We may have to get the atlas if we only have the placeholder
            if (pair.getSecond() == ATLAS_PLACEHOLDER)
            {
                pair.setSecond(manager.get("spritesheets/" + pair.getFirst() + ".atlas", TextureAtlas.class));
            }

            regions.addAll(pair.getSecond().findRegions(name));

            // Check if we found the region
            if (regions.size > 0)
            {
                // Cache the result
                cachedRegions.put(name, regions);

                return new TextureRegionsHolder(regions, name);
            }
        }

        return new TextureRegionsHolder(regions, name);
    }

    public static Array<TextureAtlas.AtlasRegion> getAllRegionsFromAltas(String atlas)
    {
        Array<TextureAtlas.AtlasRegion> result = new Array<TextureAtlas.AtlasRegion>();

        // No atlas with the name found
        if (!loadedSpritesheets.containsKey(atlas))
        {
            // Return empty list
            return result;
        }

        Pair<String, TextureAtlas> pair = loadedSpritesheets.get(atlas);

        // Check if the current atlas is loaded
        if (!manager.isLoaded("spritesheets/" + pair.getFirst() + ".atlas"))
        {
            // The atlas isn't loaded, so we block here
            manager.finishLoading();
        }

        //  We may have to get the atlas if we only have the placeholder
        if (pair.getSecond() == ATLAS_PLACEHOLDER)
        {
            pair.setSecond(manager.get("spritesheets/" + pair.getFirst() + ".atlas", TextureAtlas.class));
        }

        result.addAll(pair.getSecond().getRegions());
        return result;
    }

    public static Array<TextureAtlas.AtlasRegion> getAllRegions()
    {
        Array<TextureAtlas.AtlasRegion> regions = new Array<TextureAtlas.AtlasRegion>();

        for (String atlas: loadedSpritesheets.keys())
        {
            regions.addAll(getAllRegionsFromAltas(atlas));
        }

        return regions;
    }

    public static Array<String> getLoadedSpritesheets()
    {
        return loadedSpritesheets.keys().toArray();
    }

    public static TextureRegionHolder findRegion(String name)
    {
        Holder<Array<TextureRegion>> regions = findRegions(name);
        if (regions.data.size == 0)
        {
            return new TextureRegionHolder(name);
        }
        else
        {
            return new TextureRegionHolder(regions.data.first(), name);
        }
    }

    public static AnimationHolder createAnimation(String name, float frameDuration, Animation.PlayMode playMode)
    {
        Holder<Array<TextureRegion>> regions = findRegions(name);
        if (!regions.isValid())
        {
            return new AnimationHolder(name);
        }
        Animation animation = new Animation(frameDuration, regions.data, playMode);
        return new AnimationHolder(animation, name);
    }

    public abstract static class Holder<T>
    {
        public T data;
        public String name;

        public Holder(T data, String name)
        {
            this.data = data;
            this.name = name;
        }

        public Holder(String name)
        {
            this(null, name);
        }

        public Holder()
        {
            this(null, "null");
        }

        public boolean isValid()
        {
            return data != null;
        }
    }

    public static final class TextureRegionHolder extends Holder<TextureRegion>
    {
        public TextureRegionHolder(TextureRegion data, String name)
        {
            super(data, name);
        }

        public TextureRegionHolder(String name)
        {
            super(name);
        }

        public TextureRegionHolder()
        {
        }
    }

    public static final class TextureRegionsHolder extends Holder<Array<TextureRegion>>
    {
        public TextureRegionsHolder(Array<TextureRegion> data, String name)
        {
            super(data, name);
        }

        public TextureRegionsHolder(String name)
        {
            super(name);
        }

        public TextureRegionsHolder()
        {
        }
    }

    public static final class AnimationHolder extends Holder<Animation>
    {
        public AnimationHolder(Animation data, String name)
        {
            super(data, name);
        }

        public AnimationHolder(String name)
        {
            super(name);
        }

        public AnimationHolder()
        {
        }
    }

    public static void update()
    {
        manager.update();
    }

    public static void finishAll()
    {
        manager.finishLoading();
    }

    public static void clear()
    {
        manager.clear();
        cachedRegions.clear();
        loadedSpritesheets.clear();
        manager.finishLoading();

        System.out.println("Cleared assets");
    }
}