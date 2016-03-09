package de.project.ice.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ObjectMap
import java.util.*

object Assets {
    var manager = AssetManager()
    private val cachedRegions = ObjectMap<String, Array<TextureRegion>>()
    private val loadedSpritesheets = ObjectMap<String, Holder.TextureAtlas>()
    private val loadedAssets = HashSet<Holder<*>>()

    private fun getAtlas(name: String): Holder.TextureAtlas {
        return Holder.TextureAtlas(name, manager.get("spritesheets/$name.atlas", com.badlogic.gdx.graphics.g2d.TextureAtlas::class.java))
    }

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

        loadedSpritesheets.put(name, getAtlas(name))


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

        return Holder.Animation(name, Animation(frameDuration, if (regions.isValid) regions.data else Array(0), playMode))
    }

    open class Holder<T> constructor(val name: String = "null", open val data: T? = null) {
        open val isValid = data != null
        var isInvalidated = false
            private set

        fun invalidate() { isInvalidated = true }

        init {
            Assets.loadedAssets.add(this)
        }

        class TextureRegion(name: String = "null", data: com.badlogic.gdx.graphics.g2d.TextureRegion? = null)
            : Holder<com.badlogic.gdx.graphics.g2d.TextureRegion>(name, data)

        class TextureRegions(name: String = "null", data: Array<com.badlogic.gdx.graphics.g2d.TextureRegion>? = null)
            : Holder<Array<com.badlogic.gdx.graphics.g2d.TextureRegion>>(name, data)

        class Animation(name: String = "null",
                        data: com.badlogic.gdx.graphics.g2d.Animation = com.badlogic.gdx.graphics.g2d.Animation(0.0f, Array(0)))
            : Holder<com.badlogic.gdx.graphics.g2d.Animation>(name, data) {
            override val isValid = data.keyFrames.isNotEmpty()
            override val data: com.badlogic.gdx.graphics.g2d.Animation
                get() = super.data!!
        }

        class TextureAtlas(name: String = "null", data: com.badlogic.gdx.graphics.g2d.TextureAtlas? = null)
            : Holder<com.badlogic.gdx.graphics.g2d.TextureAtlas>(name, data)
    }

    fun reload(holder: Holder.Animation): Holder.Animation {
        return createAnimation(holder.name, holder.data.frameDuration, holder.data.playMode)
    }

    fun reload(holder: Holder.TextureRegion): Holder.TextureRegion {
        return findRegion(holder.name)
    }

    fun reload(holder: Holder.TextureRegions): Holder.TextureRegions {
        return findRegions(holder.name)
    }

    fun reload(holder: Holder.TextureAtlas): Holder.TextureAtlas {
        loadAtlas(holder.name)
        return getAtlas(holder.name)
    }


    fun update() {
        manager.update()
        loadedAssets.removeAll(loadedAssets.filter { it.isInvalidated })
    }

    fun finishAll() {
        manager.finishLoading()
    }

    fun clear() {
        manager.clear()
        cachedRegions.clear()
        loadedSpritesheets.clear()
        manager.finishLoading()
        loadedAssets.forEach { it.invalidate() }
        loadedAssets.clear()

        println("Cleared assets")
    }
}