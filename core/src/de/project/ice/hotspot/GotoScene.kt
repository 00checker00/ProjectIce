package de.project.ice.hotspot

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import de.project.ice.IceGame
import de.project.ice.ecs.Components
import de.project.ice.ecs.getComponent
import de.project.ice.utils.Assets
import de.project.ice.utils.SceneLoader


open class GotoScene(val scene : String) : Use.Adapter() {

    protected open val spawnpoint : String? = null

    override fun walk(game: IceGame, hotspotId: String) {
        game.engine.blendScreen(1.0f, Color.BLACK)
        game.engine.timeout(1.0f) {

            game.engine.entities.forEach {
                game.engine.removeEntity(it)
            }
            Assets.clear()
            Assets.finishAll()

            game.engine.timeout(0f) {
                val sceneProps = SceneLoader.loadScene(game.engine, Gdx.files.internal("scenes/$scene.scene"))
                for (spritesheet in sceneProps.spritesheets) {
                    Assets.loadAtlas(spritesheet)
                }
                game.engine.soundSystem.playMusic(sceneProps.music)
                game.engine.timeout(0f) {
                    game.engine.blendScreen(0.0f, Color.BLACK)
                    game.engine.timeout(0.5f) {
                        game.engine.blendScreen(1.0f, Color.WHITE, Color.BLACK)
                    }
                    if(spawnpoint!=null) {
                        val target = game.engine.getEntityByName(spawnpoint!!)?.getComponent(Components.transform)!!
                        val transform = game.engine.getEntityByName("Andi_Player")?.getComponent(Components.transform)!!
                        transform.pos.set(target.pos)
                    }

                    afterSceneLoaded(game)

                }
            }

        }

    }

    open fun afterSceneLoaded(game : IceGame){

    }
}