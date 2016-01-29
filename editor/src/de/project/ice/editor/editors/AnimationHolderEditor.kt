package de.project.ice.editor.editors

import de.project.ice.editor.TextureList
import de.project.ice.utils.Assets
import de.project.ice.utils.DialogListener


class AnimationHolderEditor : HolderEditor<Animation>() {
    override fun onEdit() {
        EditAnimationDialog.showDialog(stage, "Edit animation", object : DialogListener<AnimationDialogResult> {
            override fun onResult(result: AnimationDialogResult) {
                val newHolder = Assets.createAnimation(
                        result.name,
                        result.frameTime,
                        result.playMode)
                holderData = newHolder
            }

            override fun onChange(result: AnimationDialogResult) {
            }

            override fun onCancel() {
            }
        }, value!!)
    }

    private class EditAnimationDialog(title: String, private val listener: DialogListener<AnimationDialogResult>, holder: Assets.Holder<Animation>) : VisDialog(title) {
        private val textureRegion: VisTextField
        private val textureList: TextureList
        private val frameTime: VisTextField
        private val playMode: VisSelectBox<Animation.PlayMode>
        private val okButton: VisTextButton? = null
        private val cancelButton: VisTextButton? = null

        init {

            TableUtils.setSpacingDefaults(this)

            val contentTable = contentTable

            textureRegion = VisTextField(holder.name)
            contentTable.add(VisLabel("Texture Region: "))
            contentTable.add(textureRegion).expand().fill().row()

            textureList = TextureList()
            textureList.width = java.lang.Float.MAX_VALUE

            val scrollPane = VisScrollPane(textureList)

            contentTable.add(scrollPane).maxHeight(200f).colspan(2).expandX().fillX().row()

            frameTime = VisValidatableTextField(Validators.FloatValidator())
            if (holder.data != null) {
                frameTime.text = holder.data!!.frameDuration.toString()
            }
            contentTable.add(VisLabel("Frame Time: "))
            contentTable.add(frameTime).expand().fillX().row()

            playMode = VisSelectBox<Animation.PlayMode>()
            playMode.setItems(*Animation.PlayMode.values())
            if (holder.data != null) {
                playMode.selected = holder.data!!.playMode
            }
            contentTable.add(VisLabel("Mode: "))
            contentTable.add(playMode).expand().fillX()

            add(contentTable).padTop(3f).spaceBottom(4f)
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
                listener.onResult(AnimationDialogResult(
                        textureRegion.text,
                        java.lang.Float.parseFloat(frameTime.text),
                        playMode.selected))
            } else {
                listener.onCancel()
            }
        }

        private fun addListeners() {
            textureList.addListener(object : TextureList.SelectionChangedListener {
                override fun selectionChanged(newSelection: TextureAtlas.AtlasRegion, newIndex: Int) {
                    textureRegion.text = newSelection.name
                }
            })
        }

        companion object {

            fun showDialog(stage: Stage, title: String, listener: DialogListener<AnimationDialogResult>, holder: Assets.Holder<Animation>) {
                val dialog = EditAnimationDialog(title, listener, holder)
                dialog.show(stage)
            }
        }
    }

    private class AnimationDialogResult(val name: String, val frameTime: Float, val playMode: Animation.PlayMode)
}
