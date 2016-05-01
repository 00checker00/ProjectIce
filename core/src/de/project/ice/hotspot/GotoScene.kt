package de.project.ice.hotspot

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import de.project.ice.IceGame
import de.project.ice.Storage
import de.project.ice.ecs.Components
import de.project.ice.ecs.getComponents
import de.project.ice.utils.Assets
import de.project.ice.utils.SceneLoader
import de.project.ice.utils.SceneWriter


open class GotoScene(val scene : String) : Use.Adapter() {

    protected open val spawnpoint : String? = null

    override fun walk(game: IceGame, hotspotId: String) {

        game.blockInteraction = true;

        game.engine.blendScreen(1.0f, Color.BLACK)
        game.engine.timeout(1.0f) {
            val sceneXml = SceneWriter.serializeToString(game.engine, game.engine.sceneProperties!!)
            Storage.SAVESTATE.put("${game.engine.sceneProperties!!.name}_STATE", sceneXml)

            game.engine.entities.forEach {
                game.engine.removeEntity(it)
            }

            game.engine.timeout(0f) {
                var sceneName = SceneLoader.getSceneName(game.engine, Gdx.files.internal("scenes/$scene.scene"))
                val storedState = Storage.SAVESTATE.getString("${sceneName}_STATE", "")

                val properties: SceneLoader.SceneProperties
                if (storedState.isNotBlank())
                    properties = SceneLoader.loadScene(game.engine, storedState)
                else
                    properties = SceneLoader.loadScene(game.engine, Gdx.files.internal("scenes/$scene.scene"))

                game.engine.timeout(0f) {
                    game.engine.blendScreen(0.0f, Color.BLACK)
                    game.engine.timeout(0.5f) {
                        game.engine.blendScreen(1.0f, Color.WHITE, Color.BLACK)
                    }
                    spawnpoint?.let {
                        val target = game.engine.getEntityByName(it)?.getComponents(Components.transform)
                        val transform = game.engine.getEntityByName("Andi_Player")?.getComponents(Components.transform)
                        transform?.pos?.set(target?.pos?:transform.pos)
                    }

                    afterSceneLoaded(game)
                    game.blockInteraction = false;
                }
            }

        }

    }

    open fun afterSceneLoaded(game : IceGame){

    }
}