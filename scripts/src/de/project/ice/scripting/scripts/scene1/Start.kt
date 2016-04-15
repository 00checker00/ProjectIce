package de.project.ice.scripting.scripts.scene1

import com.badlogic.ashley.core.Entity
import de.project.ice.IceGame
import de.project.ice.dialog.Node
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
            callback = { AndiWalksToKai(game) }
        })
    }

    override fun onUpdateEntity(game: IceGame, entity: Entity, delta: Float) {

        runOnce("scene1_intro") {
            game.blockInteraction = true
            game.blockSaving = true



            game.showDialog("s1_dlg_rein_fall_intro") {


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
                        game.showDialog("s1_dlg_trolaf_intro") {
                            kaiGetsPaper(game)
                        }
                    }

                }
            }


        }
    }

    private fun AndiWalksToKai(game: IceGame) {
        game.showDialog("s1_dlg_kai_intro") {

            game.engine.editEntity("Andi_Player") {
                PathPlanningComponent {
                    start = game.engine.getEntityByName("Andi_Player")?.getComponent(Components.transform)?.pos!!
                    target = game.engine.getEntityByName("go_andi_to_kai")?.getComponent(Components.transform)?.pos!!
                    callback = {
                        game.showDialog("s1_dlg_andi_intro") {
                            game.showDialog("s1_dlg_kai_intro_2"){
                                KaiTakesWrit(game)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun KaiTakesWrit(game: IceGame) {

            game.engine.editEntity("Kai") {
                PathPlanningComponent {
                    start = game.engine.getEntityByName("Kai")?.getComponent(Components.transform)?.pos!!
                    target = game.engine.getEntityByName("kai_takes_writ_andi")?.getComponent(Components.transform)?.pos!!
                    callback = {
                        game.showDialog("s1_dlg_andi_intro_2"){
                            PathPlanningComponent {
                                start = game.engine.getEntityByName("Kai")?.getComponent(Components.transform)?.pos!!
                                target = game.engine.getEntityByName("kai_writ_toss_well")?.getComponent(Components.transform)?.pos!!
                                callback = {
                                    val dialog = Node().apply {
                                        speaker = "Kai"
                                        type = Node.Type.Text
                                        text = "s1_dlg_kai_torn_to_pieces"
                                    }
                                    val handle = game.showDialog(dialog)

                                    game.engine.timeout(2.5f) {
                                        handle.cancel()
                                        game.blockInteraction = false
                                        game.blockSaving = false

                                    }

                                    game.engine.timeout(2.0f) {

                                        PathPlanningComponent {
                                            start = game.engine.getEntityByName("Kai")?.getComponent(Components.transform)?.pos!!
                                            target = game.engine.getEntityByName("kai_waypoint_1")?.getComponent(Components.transform)?.pos!!
                                            callback = {

                                                val waypoints = arrayOf(

                                                        game.engine.getEntityByName("out_main_chars")?.getComponent(Components.transform)?.pos

                                                ).filterNotNull()

                                                game.engine.editEntity("Kai") {
                                                    MoveComponent {
                                                        targetPositions.addAll(waypoints)
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