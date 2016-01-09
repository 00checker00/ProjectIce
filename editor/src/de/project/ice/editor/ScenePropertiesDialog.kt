package de.project.ice.editor

import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import com.kotcrab.vis.ui.util.TableUtils
import com.kotcrab.vis.ui.widget.VisDialog
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTextArea
import com.kotcrab.vis.ui.widget.VisTextField
import de.project.ice.ecs.IceEngine
import de.project.ice.utils.DialogListener
import de.project.ice.utils.SceneLoader
import de.project.ice.utils.join


class ScenePropertiesDialog @Throws(IllegalStateException::class)
constructor(properties: SceneLoader.SceneProperties) : VisDialog("Scene Properties") {
    private val engine: IceEngine
    private var musicText: VisTextField? = null
    private var soundText: VisTextArea? = null
    private var spritesheetsText: VisTextArea? = null
    private var onloadText: VisTextField? = null
    private var sceneNameText: VisTextField? = null
    private val dialogListeners = Array<DialogListener<SceneLoader.SceneProperties>>()

    init {
        this.engine = properties.engine()

        TableUtils.setSpacingDefaults(this)
        createWidgets()

        sceneNameText!!.text = properties.name()
        musicText!!.text = properties.music()
        onloadText!!.text = properties.onloadScript()
        soundText!!.text = properties.sounds().join("\n")
        spritesheetsText!!.text = properties.spritesheets().join("\n")

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
        contentTable.add<VisTextField>(sceneNameText).expandX().fill().row()

        val spritesheetslabel = VisLabel("Spritesheets:")
        contentTable.add(spritesheetslabel).top()
        spritesheetsText = VisTextArea()
        contentTable.add<VisTextArea>(spritesheetsText).minHeight(100f).expand().fill().row()

        val onloadlabel = VisLabel("Onload Script:")
        contentTable.add(onloadlabel)
        onloadText = VisTextField()
        contentTable.add<VisTextField>(onloadText).expandX().fill().row()

        val soundlabel = VisLabel("Sounds:")
        contentTable.add(soundlabel).top()
        soundText = VisTextArea()
        contentTable.add<VisTextArea>(soundText).minHeight(100f).expand().fill().row()

        val musiclabel = VisLabel("Musik:")
        contentTable.add(musiclabel)
        musicText = VisTextField()
        contentTable.add<VisTextField>(musicText).expandX().fill().row()


        button("Ok", "ok")
        button("Cancel", "cancel")
    }

    override fun result(`object`: Any?) {
        if ("ok" == `object`) {
            val sceneProperties = SceneLoader.ScenePropertiesBuilder().engine(engine).name(sceneNameText!!.text).music(musicText!!.text).onloadScript(onloadText!!.text).sounds(Array(soundText!!.text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())).spritesheets(Array(spritesheetsText!!.text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())).create()

            for (listener in dialogListeners) {
                listener.onResult(sceneProperties)
            }
        } else {
            for (listener in dialogListeners) {
                listener.onCancel()
            }
        }
    }
}