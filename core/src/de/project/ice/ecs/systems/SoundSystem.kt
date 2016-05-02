package de.project.ice.ecs.systems

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.DelayedRemovalArray
import com.badlogic.gdx.utils.LongMap
import com.badlogic.gdx.utils.ObjectMap


class SoundSystem : IceSystem() {
    private val sounds = ObjectMap<String, Sound>()
    private val ids = LongMap<String>()
    private var music: Music? = null
    private val faders = DelayedRemovalArray<Fade>()
    var musicname = ""; private set
    var musicvolume = 1f; private set

    enum class Type {
        Voice,
        Effect
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        faders.begin()

        for (fade in faders) {
            if (fade.update(deltaTime)) {
                faders.removeValue(fade, true)
            }
        }

        faders.end()


    }

    fun loadSound(name: String, type: Type = Type.Effect): Boolean {
        val dir: String
        if (type == Type.Effect) {
            dir = "sounds"
        } else {
            dir = "voices"
        }
        var file = Gdx.files.internal("$dir/$name.mp3")

        if (!file.exists()) {
            file = Gdx.files.internal("$dir/$name.ogg")
            if (!file.exists()) {
                Gdx.app.log(javaClass.simpleName, "Sound file doesn't exist: " + file.pathWithoutExtension() + " {.ogg, .mp3} ")
                return false
            }
        }

        val sound = Gdx.audio.newSound(file)
        sounds.put(name + "_" + type.toString(), sound)

        return true
    }

    fun playSound(name: String, type: Type = Type.Effect): Long {
        if (!sounds.containsKey(name + "_" + type.toString())) {
            if (!loadSound(name, type)) {
                return -1
            }
        }
        val sound = sounds.get(name + "_" + type.toString())
        val playID = sound.play()
        ids.put(playID, name + "_" + type.toString())
        return playID
    }

    fun stopSound(playID: Long) {
        sounds.get(ids.get(playID, ""))?.stop(playID)

    }

    fun resumeSound(playID: Long) {
        sounds.get(ids.get(playID, ""))?.resume(playID)
    }

    fun pauseSound(playID: Long) {
        sounds.get(ids.get(playID, ""))?.pause(playID)
    }

    fun unloadSounds() {
        ids.clear()

        for (sound in sounds.values()) {
            sound.dispose()
        }

        sounds.clear()
    }

    fun getSounds(): Array<String> {
        return sounds.keys().toArray()
    }


    fun playMusic(name: String, loop: Boolean = true, volume: Float = 1.0f) {
        val music: Music

        if (name == musicname) {
            music = this.music!!
            music.volume = volume
        } else {
            var file = Gdx.files.internal("music/$name.mp3")
            if (!file.exists()) {
                file = Gdx.files.internal("music/$name.ogg")
                if (!file.exists()) {
                    Gdx.app.log(javaClass.simpleName, "Sound file doesn't exist: " + file.pathWithoutExtension() + " {.ogg, .mp3} ")
                    return
                }
            }
            music = Gdx.audio.newMusic(file)
        }

        music.isLooping = loop

        musicname = name
        musicvolume = volume

        this.music?.apply {
            if (this != music) {
                faders.add(Fade.fadeCross(this, music, volume))
            }

        } ?: let {
            faders.add(Fade.fadeIn(music, volume))
        }

        this.music = music

        music.play()
    }

    fun stopMusic() {
        this.music?.apply {
            faders.add(Fade.fadeOut(this))
            music = null
            musicname = ""
        }
    }

    fun pauseMusic() {
        music?.pause()
    }

    fun resumeMusic() {
        music?.play()
    }

    internal open class Fade internal constructor(protected var music: Music, protected var start: Float, protected var end: Float) {
        protected var alpha: Float = 0.toFloat()

        init {
            this.alpha = 0f
            music.volume = start
        }

        open fun update(delta: Float): Boolean {
            alpha += delta / DURATION
            val volume = Interpolation.pow2.apply(start, end, alpha)
            music.volume = volume

            return alpha >= 1f
        }

        companion object {
            protected var DURATION = 3f

            fun fadeIn(music: Music, volume: Float = 1.0f): Fade {
                return Fade(music, 0f, volume)
            }

            fun fadeOut(music: Music): Fade {
                return FadeOut(music, music.volume, 0f)
            }

            fun fadeCross(first: Music, second: Music, volume: Float = 1.0f): Fade {
                return FadeCross(first, second, volume)
            }
        }
    }

    internal open class FadeOut internal constructor(music: Music, start: Float, end: Float) : Fade(music, start, end) {

        override fun update(delta: Float): Boolean {
            if (super.update(delta)) {
                music.stop()
                music.dispose()
                return true
            }
            return false
        }
    }

    private class FadeCross internal constructor(first: Music, second: Music, volume: Float = 1.0f) : FadeOut(first, first.volume, 0f) {
        private val fadeIn = Fade.fadeIn(second, volume)

        override fun update(delta: Float): Boolean {
            return super.update(delta) && fadeIn.update(delta)
        }
    }

}

