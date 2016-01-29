package de.project.ice.editor.editors

import de.project.ice.editor.TextureList
import de.project.ice.utils.Assets
import de.project.ice.utils.DialogListener


class TextureRegionHolderEditor : HolderEditor<TextureRegion>() {
    override fun onEdit() {
        val currentTexture = value!!.name
        EditTextureRegionDialog.showDialog(stage, "Select texture", object : DialogListener<String> {
            override fun onResult(textureRegion: String) {
                val newHolder = Assets.findRegion(textureRegion)
                holderData = newHolder
            }

            override fun onChange(textureRegion: String) {
                val newHolder = Assets.findRegion(textureRegion)
                holderData = newHolder
            }

            override fun onCancel() {
                val newHolder = Assets.findRegion(currentTexture)
                holderData = newHolder
            }
        }, value!!.name)
    }

    private class EditTextureRegionDialog(title: String, private val listener: DialogListener<String>, currentTexture: String) : VisDialog(title) {
        private val textureRegion: VisTextField
        private val textureList: TextureList

        init {

            TableUtils.setSpacingDefaults(this)

            addCloseButton()

            val contentTable = contentTable

            textureRegion = VisTextField(currentTexture)
            contentTable.add(VisLabel("Texture Region: "))
            contentTable.add(textureRegion).expandX().fill().row()

            textureList = TextureList()
            textureList.width = java.lang.Float.MAX_VALUE

            val scrollPane = VisScrollPane(textureList)

            contentTable.add(scrollPane).maxHeight(200f).colspan(2).expandX().fill().row()

            row()

            addListeners()

            textureRegion.focusField()

            button("cancel")
            key(Input.Keys.ESCAPE, "cancel")
            button("ok")
            key(Input.Keys.ENTER, "ok")
        }

        override fun show(stage: Stage, action: Action?): VisDialog {
            val result = super.show(stage, action)
            stage.scrollFocus = textureList
            return result
        }

        override fun result(`object`: Any?) {
            if ("ok" == `object`) {
                listener.onResult(textureRegion.text)
            } else {
                listener.onCancel()
            }
        }

        private fun addListeners() {
            textureRegion.addListener(object : InputListener() {
                override fun keyUp(event: InputEvent?, keycode: Int): Boolean {
                    listener.onChange(textureRegion.text)
                    return super.keyUp(event, keycode)
                }
            })

            textureList.addListener(object : TextureList.SelectionChangedListener {
                override fun selectionChanged(newSelection: TextureAtlas.AtlasRegion, newIndex: Int) {
                    listener.onChange(newSelection.name)
                    textureRegion.text = newSelection.name
                }
            })
        }

        companion object {


            fun showDialog(stage: Stage, title: String, listener: DialogListener<String>, currentTexture: String) {
                val dialog = EditTextureRegionDialog(title, listener, currentTexture)
                dialog.show(stage)
            }
        }
    }
}
