package de.project.ice.scripting.scripts.scene1

import com.badlogic.ashley.core.Entity
import de.project.ice.IceGame
import de.project.ice.ecs.Components
import de.project.ice.ecs.components.MoveComponent
import de.project.ice.ecs.getComponent
import de.project.ice.scripting.Script
import de.project.ice.scripting.blockInteraction
import de.project.ice.scripting.blockSaving
import de.project.ice.scripting.runOnce
import de.project.ice.utils.editEntity

/**
 * Created by rftpool24 on 15.03.2016.
 */
class Start : Script() {


    fun kaiGetsPaper(game: IceGame) {
        val waypoints = arrayOf(
                game.engine.getEntityByName("kai_waypoint_1")?.getComponent(Components.transform)?.pos,
                game.engine.getEntityByName("kai_waypoint_2")?.getComponent(Components.transform)?.pos,
                game.engine.getEntityByName("kai_waypoint_3")?.getComponent(Components.transform)?.pos,
                game.engine.getEntityByName("kai_waypoint_4")?.getComponent(Components.transform)?.pos,
                game.engine.getEntityByName("kai_waypoint_5")?.getComponent(Components.transform)?.pos,
                game.engine.getEntityByName("kai_waypoint_6")?.getComponent(Components.transform)?.pos
        ).filterNotNull()

        game.engine.getEntityByName("Kai")?.add(game.engine.createComponent(MoveComponent::class.java).apply {
            targetPositions.addAll(waypoints)
        })
    }

    override fun onUpdateEntity(game: IceGame, entity: Entity, delta: Float) {

        runOnce("scene1_intro") {
            game.BlockInteraction = true
            game.BlockSaving = true

            game.blockInteraction {
                game.blockSaving {

                    game.showDialog("s1_dlg_intro") {
                        game.engine.soundSystem.playSound("s1_dlg_rein_fall_intro_1")

                        runOnce("falls_went_to_igloo") {

                            game.engine.editEntity("Klara Fall") {
                                PathPlanningComponent {
                                    speed = 1.0f
                                    start = game.engine.getEntityByName("Klara Fall")?.getComponent(Components.transform)?.pos!!
                                    target = game.engine.getEntityByName("out_fall_igloo")?.getComponent(Components.transform)?.pos!!
                                    callback = {
                                        game.engine.removeEntity("Klara Fall")
                                    }
                                }
                            }

                            game.engine.timeout(0.5f) {
                                game.engine.editEntity("Rein Fall") {
                                    PathPlanningComponent {
                                        speed = 0.7f
                                        start = game.engine.getEntityByName("Rein Fall")?.getComponent(Components.transform)?.pos!!
                                        target = game.engine.getEntityByName("out_fall_igloo")?.getComponent(Components.transform)?.pos!!
                                        callback = {
                                            game.engine.removeEntity("Rein Fall")
                                        }
                                    }
                                }
                            }

                            game.engine.timeout(1.0f) {
                                game.showDialog("s1_dlg_intro") {
                                    kaiGetsPaper(game)
                                }
                            }

                            game.engine.timeout(1.0f) {
                                game.showDialog("s1_dlg_intro") {

                                    game.engine.editEntity("Andi_Player") {
                                        PathPlanningComponent {
                                            speed = 1.0f
                                            start = game.engine.getEntityByName("Andi_Player")?.getComponent(Components.transform)?.pos!!
                                            target = game.engine.getEntityByName("go_andi_to_kai")?.getComponent(Components.transform)?.pos!!
                                            callback = {
                                                game.engine.editEntity("Kai") {

                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }


        }
    }
}