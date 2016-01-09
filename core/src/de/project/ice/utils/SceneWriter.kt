package de.project.ice.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.IntMap
import com.badlogic.gdx.utils.XmlWriter
import com.badlogic.gdx.utils.reflect.ReflectionException
import de.project.ice.ecs.IceEngine
import de.project.ice.ecs.systems.SoundSystem
import de.project.ice.pathlib.PathArea
import de.project.ice.pathlib.Shape
import java.io.IOException
import kotlin.reflect.jvm.javaField
import kotlin.reflect.memberProperties

class SceneWriter private constructor(private val sceneName: String, private val onloadScript: String, private val engine: IceEngine, private val xml: XmlWriter) {

    @Throws(IOException::class)
    fun serializeScene() {
        xml.element("scene").attribute("name", sceneName).attribute("onload", onloadScript)


        serializeAudio(xml, engine.soundSystem)
        serializeSpritesheets(xml)

        xml.element("entities")
        for (entity in engine.entities) {
            xml.element("entity")
            xml.element("components")
            for (component in entity.components) {
                xml.element(component.javaClass.simpleName)
                for (property in component.javaClass.kotlin.memberProperties) {
                    val f = property.javaField;

                    if (f == null) {
                        Gdx.app.log("SceneWriter", "No baking field found for property ${property.name} of component ${component.javaClass.simpleName}")
                        continue;
                    }

                    f.isAccessible = true

                    val value = f.get(component) ?: continue

                    xml.element(f.name)
                    try {
                        serializeObject(xml, value)
                    } catch (e: ReflectionException) {
                        e.printStackTrace()
                    }

                    xml.pop()
                }
                xml.pop()
            }
            xml.pop()
            xml.pop()
        }

        xml.pop()
        xml.pop()
        xml.close()
    }

    @Throws(IOException::class)
    private fun serializeSpritesheets(xml: XmlWriter) {
        xml.element("spritesheets")

        for (s in Assets.getLoadedSpritesheets()) {
            xml.element("spritesheet").text(s).pop()
        }

        xml.pop()
    }

    @Throws(IOException::class)
    private fun serializeAudio(xml: XmlWriter, sound: SoundSystem) {
        xml.element("music").text(sound.music).pop()

        xml.element("sounds")

        for (s in sound.sounds) {
            xml.element("sound").text(s).pop()
        }

        xml.pop()

    }

    @Throws(IOException::class)
    private fun serializeObject(xml: XmlWriter, o: Any) {
        when (o) {
            is Float -> {
                xml.attribute("type", "Float");  xml.text(o)
            }
            is Double -> {
                xml.attribute("type", "Double");  xml.text(o)
            }
            is Short -> {
                xml.attribute("type", "Short");  xml.text(o)
            }
            is Int -> {
                xml.attribute("type", "Integer");  xml.text(o)
            }
            is Long -> {
                xml.attribute("type", "Long");  xml.text(o)
            }
            is String -> {
                xml.attribute("type", "String");  xml.text(o.replace("&", "&amp").replace("<", "&lt").replace(">", "&gt"))
            }
            is Boolean -> {
                xml.attribute("type", "Boolean");  xml.text(if (o) "true" else "false")
            }
            is Vector2 -> {
                xml.attribute("type", o.javaClass.simpleName);  xml.attribute("x", o.x).attribute("y", o.y)
            }
            is Vector3 -> {
                xml.attribute("type", o.javaClass.simpleName);  xml.attribute("x", o.x).attribute("y", o.y).attribute("z", o.z)
            }
            is Assets.Holder.Animation -> {
                xml.attribute("type", o.javaClass.simpleName)
                if (o.data != null) xml
                        .attribute("frameDuration", o.data.animationDuration)
                        .attribute("mode", o.data.playMode).text(o.name)
            }
            is Assets.Holder.TextureRegion -> {
                xml.attribute("type", o.javaClass.simpleName); xml.text(o.name);
            }
            is IntMap<*> -> {
                xml.attribute("type", o.javaClass.simpleName)
                for (entry in o) if (entry.value != null) {
                    xml.attribute("subType", entry.value.javaClass.simpleName)
                    break
                }
                for (entry in o) {
                    xml.element("_${entry.key}")
                    serializeObject(xml, entry.value)
                    xml.pop()
                }
            }
            is OrthographicCamera -> {
                xml.attribute("type", o.javaClass.simpleName)
                xml.attribute("viewportWidth", o.viewportWidth)
                        .attribute("viewportHeight", o.viewportHeight)
                        .element("pos")
                serializeObject(xml, o.position)
                xml.pop()
            }
            is PathArea -> {
                xml.attribute("type", o.javaClass.simpleName)
                xml.element("shape")
                serializeObject(xml, o.shape)
                xml.pop()

                xml.element("holes")
                for (hole in o.holes) {
                    xml.element("hole")
                    serializeObject(xml, hole)
                    xml.pop()
                }
                xml.pop()
            }
            is Shape -> {
                xml.attribute("type", o.javaClass.simpleName)
                xml.attribute("closed", o.closed)
                xml.element("vertices")
                for (v in o.vertices) {
                    xml.element("vertex")
                    serializeObject(xml, v)
                    xml.pop()
                }
                xml.pop()
            }
            else -> {
                xml.attribute("type", "null")
                return
            }
        }
    }

    class Builder {
        private var sceneName = ""
        private var onloadScript = ""
        private var engine: IceEngine? = null
        private var writer: XmlWriter? = null

        fun sceneName(sceneName: String): Builder {
            this.sceneName = sceneName
            return this
        }

        fun onloadScript(onloadScript: String): Builder {
            this.onloadScript = onloadScript
            return this
        }

        fun engine(engine: IceEngine): Builder {
            this.engine = engine
            return this
        }

        fun writer(writer: XmlWriter): Builder {
            this.writer = writer
            return this
        }

        fun create(): SceneWriter {
            if (engine == null || writer == null) {
                throw RuntimeException("Trying to create SceneWriter without XmlWriter or Engine")
            }
            return SceneWriter(sceneName, onloadScript, engine!!, writer!!)
        }
    }
}
