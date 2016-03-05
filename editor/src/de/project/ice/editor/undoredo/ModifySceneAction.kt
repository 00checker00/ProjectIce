package de.project.ice.editor.undoredo

import de.project.ice.ecs.IceEngine
import de.project.ice.utils.SceneLoader
import de.project.ice.utils.SceneWriter



class ModifySceneAction(private val engine: IceEngine,
                        private val before: String,
                        private val after: String): UndoableRedoableAction {

    override fun undo() {
        engine.removeAllEntities()
        engine.update(0f)
        SceneLoader.loadScene(engine, before);
    }

    override fun redo() {
        engine.removeAllEntities()
        engine.update(0f)
        SceneLoader.loadScene(engine, after);
    }

    class Recording internal constructor(private val engine: IceEngine,
                                        private val  properties: SceneLoader.SceneProperties) {
        private val before = SceneWriter.serializeToString(engine, properties)

        fun finish(): ModifySceneAction
                = ModifySceneAction(engine, before, SceneWriter.serializeToString(engine, properties))
    }

    companion object {
        fun record(engine: IceEngine,
        properties: SceneLoader.SceneProperties,
        modification: (IceEngine)->Unit) : ModifySceneAction {
            val before = SceneWriter.serializeToString(engine, properties)
            modification.invoke(engine)
            val after = SceneWriter.serializeToString(engine, properties)
            return ModifySceneAction(engine, before, after)
        }

        fun startRecording(engine: IceEngine,
                   properties: SceneLoader.SceneProperties)
            = Recording(engine, properties)
    }
}
