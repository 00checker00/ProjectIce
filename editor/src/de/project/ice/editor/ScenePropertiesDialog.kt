package de.project.ice.editor

import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import com.kotcrab.vis.ui.util.TableUtils
import com.kotcrab.vis.ui.widget.*
import de.project.ice.ecs.IceEngine
import de.project.ice.utils.DialogListener
import de.project.ice.utils.SceneLoader
import de.project.ice.utils.join


class ScenePropertiesDialog @Throws(IllegalStateException::class)
constructor(private val properties: SceneLoader.SceneProperties) : VisDialog("Scene Properties") {
    private val engine: IceEngine
    private var musicText: VisTextField? = null
    private var spritesheetsText: VisTextArea? = null
    private var volumeSlider: VisSlider? = null
    private val dialogListeners = Array<DialogListener<SceneLoader.SceneProperties>>()
    private var sceneNameText: VisTextField? = null

    init {
        this.engine = properties.engine

        TableUtils.setSpacingDefaults(this)
        createWidgets()

        sceneNameText!!.text = properties.name
        musicText!!.text = properties.music
        spritesheetsText!!.text = properties.spritesheets.join("\n")
        volumeSlider!!.value = properties.musicVolume

        setPosition(0f, 0f, Align.topRight)
        isResizable = true
        setSize(300f, 400f)
    }

    fun addListener(listener: DialogListener<SceneLoader.SceneProperties>): ScenePropertiesDialog {
        dialogListeners.add(listener)
        return this
    }

    fun removeListener(listener: DialogListener<SceneLoader.SceneProperties>): ScenePropertiesDialog {
        dialogListeners.removeValue(listener, true)
        return this
    }

    private fun createWidgets() {
        val contentTable = contentTable

        val sceneNamelabel = VisLabel("Name:")
        contentTable.add(sceneNamelabel)
        sceneNameText = VisTextField()
        contentTable.add(sceneNameText).expandX().fill().row()

        val spritesheetslabel = VisLabel("Spritesheets:")
        contentTable.add(spritesheetslabel).top()
        spritesheetsText = VisTextArea()
        contentTable.add<VisTextArea>(spritesheetsText).minHeight(100f).expand().fill().row()

        val musiclabel = VisLabel("Musik:")
        contentTable.add(musiclabel)
        musicText = VisTextField()
        contentTable.add(musicText).expandX().fill().row()

        val volumeLabel = VisLabel("Volume:")
        contentTable.add(volumeLabel)
        volumeSlider = VisSlider(0f, 1f, 0.01f, false)
        contentTable.add(volumeSlider).expandX().fill().row()



        button("Ok", "ok")
        button("Cancel", "cancel")
    }

    override fun result(`object`: Any?) {
        if ("ok" == `object`) {
            properties.apply {
                engine = engine
                name = sceneNameText!!.text
                music = musicText!!.text
                musicVolume = volumeSlider!!.value
                spritesheets = Array(spritesheetsText!!.text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
            }


            for (listener in dialogListeners) {
                listener.onResult(properties)
            }
        } else {
            for (listener in dialogListeners) {
                listener.onCancel()
            }
        }
    }
}