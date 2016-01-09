package de.project.ice.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ObjectMap

object Assets {
    var manager = AssetManager()
    private val cachedRegions = ObjectMap<String, Array<TextureRegion>>()
    private val loadedSpritesheets = ObjectMap<String, Holder.TextureAtlas>()

    fun loadAtlas(name: String): Boolean {
        if (loadedSpritesheets.containsKey(name)) {
            return true
        }

        val file = Gdx.files.internal("spritesheets/$name.atlas")

        if (!file.exists()) {
            Gdx.app.log(Assets::class.java.simpleName, "Unable to load spritesheet \"${file.path()}\": File not found")
            return false
        }

        manager.load(file.path(), TextureAtlas::class.java)
        manager.finishLoadingAsset(file.path())

        loadedSpritesheets.put(name, Holder.TextureAtlas(name, manager.get("spritesheets/${name}.atlas", com.badlogic.gdx.graphics.g2d.TextureAtlas::class.java)))


        println("Loaded assets: " + name)
        return true
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

    fun findRegions(name: String): Holder.TextureRegions {
        // Look if region is already cached
        if (cachedRegions.containsKey(name)) {
            return Holder.TextureRegions(name, cachedRegions.get(name))
        }


        // The region isn't cached, so lets try to find it
        val regions = Array<TextureRegion>()
        for (atlas in loadedSpritesheets.values()) {
            // Check if the current atlas is loaded
            if (!manager.isLoaded("spritesheets/${atlas.name}.atlas")) {
                // The atlas isn't loaded, so we block here
                manager.finishLoading()
            }

            if (atlas.data == null) {
                continue
            }

            regions.addAll(atlas.data.findRegions(name))

            // Check if we found the region
            if (regions.size > 0) {
                // Cache the result
                cachedRegions.put(name, regions)

                return Holder.TextureRegions(name, regions)
            }
        }

        return Holder.TextureRegions(name, regions)
    }

    fun getAllRegionsFromAltas(atlasName: String): Array<TextureAtlas.AtlasRegion> {
        val result = Array<TextureAtlas.AtlasRegion>()

        // No atlas with the name found
        if (!loadedSpritesheets.containsKey(atlasName)) {
            // Return empty list
            return result
        }

        val atlas = loadedSpritesheets.get(atlasName)

        // Check if the current atlas is loaded
        if (!manager.isLoaded("spritesheets/" + atlas.name + ".atlas")) {
            // The atlas isn't loaded, so we block here
            manager.finishLoading()
        }

        //  We may have to get the atlas if we only have the placeholder
        if (atlas.data != null) {
            result.addAll(atlas.data.regions)
        }

        return result
    }

    val allRegions: Array<TextureAtlas.AtlasRegion>
        get() {
            val regions = Array<TextureAtlas.AtlasRegion>()

            for (atlas in loadedSpritesheets.keys()) {
                regions.addAll(getAllRegionsFromAltas(atlas))
            }

            return regions
        }

    fun getLoadedSpritesheets(): Array<String> {
        return loadedSpritesheets.keys().toArray()
    }

    fun findRegion(name: String): Holder.TextureRegion {
        val regions = findRegions(name)
        if (regions.data!!.size == 0) {
            return Holder.TextureRegion(name)
        } else {
            return Holder.TextureRegion(name, regions.data.first())
        }
    }

    fun createAnimation(name: String, frameDuration: Float, playMode: Animation.PlayMode): Holder.Animation {
        val regions = findRegions(name)
        if (!regions.isValid) {
            return Holder.Animation(name)
        }
        val animation = Animation(frameDuration, regions.data, playMode)
        return Holder.Animation(name, animation)
    }

    open class Holder<T> constructor(val name: String = "null", val data: T? = null) {
        val isValid = data != null

        class TextureRegion(name: String = "null", data: com.badlogic.gdx.graphics.g2d.TextureRegion? = null) : Holder<com.badlogic.gdx.graphics.g2d.TextureRegion>(name, data)
        class TextureRegions(name: String = "null", data: Array<com.badlogic.gdx.graphics.g2d.TextureRegion>? = null) : Holder<Array<com.badlogic.gdx.graphics.g2d.TextureRegion>>(name, data)
        class Animation(name: String = "null", data: com.badlogic.gdx.graphics.g2d.Animation? = null) : Holder<com.badlogic.gdx.graphics.g2d.Animation>(name, data)
        class TextureAtlas(name: String = "null", data: com.badlogic.gdx.graphics.g2d.TextureAtlas? = null) : Holder<com.badlogic.gdx.graphics.g2d.TextureAtlas>(name, data)
    }


    fun update() {
        manager.update()
    }

    fun finishAll() {
        manager.finishLoading()
    }

    fun clear() {
        manager.clear()
        cachedRegions.clear()
        loadedSpritesheets.clear()
        manager.finishLoading()

        println("Cleared assets")
    }
}