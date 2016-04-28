package de.project.ice.scripting.scripts.scene1

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import de.project.ice.IceGame
import de.project.ice.dialog.Node
import de.project.ice.ecs.Components
import de.project.ice.ecs.components.MoveComponent
import de.project.ice.ecs.getComponents
import de.project.ice.scripting.Script
import de.project.ice.scripting.blockInteraction
import de.project.ice.scripting.blockSaving
import de.project.ice.scripting.runOnce
import de.project.ice.utils.Assets
import de.project.ice.utils.editEntity

/**
 * Created by rftpool24 on 15.03.2016.
 */
class Start : Script() {


    fun kaiGetsPaper(game: IceGame) {
        val waypoints = arrayOf(
                game.engine.getEntityByName("kai_waypoint_1")?.getComponents(Components.transform)?.pos,
                game.engine.getEntityByName("kai_waypoint_2")?.getComponents(Components.transform)?.pos,
                game.engine.getEntityByName("kai_waypoint_3")?.getComponents(Components.transform)?.pos,
                game.engine.getEntityByName("kai_waypoint_4")?.getComponents(Components.transform)?.pos,
                game.engine.getEntityByName("kai_waypoint_5")?.getComponents(Components.transform)?.pos,
                game.engine.getEntityByName("kai_waypoint_6")?.getComponents(Components.transform)?.pos
        ).filterNotNull()

        game.engine.getEntityByName("Kai")?.add(game.engine.createComponent(MoveComponent::class.java).apply {
            targetPositions.addAll(waypoints)
            callback = { AndiWalksToKai(game) }
        })
    }

    override fun onUpdateEntity(game: IceGame, entity: Entity, delta: Float) {

        runOnce("scene1_intro") {
            game.engine.soundSystem.playMusic("11", true, 0.5f);

            game.blockInteraction = true
            game.blockSaving = true


            game.showDialog("s1_dlg_rein_fall_intro") {


                runOnce("falls_went_to_igloo") {

                    game.engine.editEntity("Klara Fall") {
                        PathPlanningComponent {
                            speed = 1.1f
                            start = game.engine.getEntityByName("Klara Fall")?.getComponents(Components.transform)?.pos!!
                            target = game.engine.getEntityByName("out_fall_igloo")?.getComponents(Components.transform)?.pos!!
                            callback = {
                                game.engine.removeEntity("Klara Fall")
                            }
                        }
                    }

                    game.engine.timeout(0.5f) {
                        game.engine.editEntity("Rein Fall") {
                            PathPlanningComponent {
                                speed = 0.9f
                                start = game.engine.getEntityByName("Rein Fall")?.getComponents(Components.transform)?.pos!!
                                target = game.engine.getEntityByName("out_fall_igloo")?.getComponents(Components.transform)?.pos!!
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
                    speed = game.engine.getEntityByName("Andi_Player")?.getComponents(Components.control)?.speed!!
                    start = game.engine.getEntityByName("Andi_Player")?.getComponents(Components.transform)?.pos!!
                    target = game.engine.getEntityByName("go_andi_to_kai")?.getComponents(Components.transform)?.pos!!
                    callback = {
                        game.engine.getEntityByName("Andi_Player")?.getComponents(Components.transform)?.flipHorizontal = false
                        val urne = game.engine.getEntityByName("Urne")
                        val urntex = urne?.getComponents((Components.texture))
                        urntex?.region = Assets.findRegion("urne_ohneBrief")

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
                    start = game.engine.getEntityByName("Kai")?.getComponents(Components.transform)?.pos!!
                    target = game.engine.getEntityByName("kai_takes_writ_andi")?.getComponents(Components.transform)?.pos!!
                    callback = {
                        game.showDialog("s1_dlg_andi_intro_2"){
                            PathPlanningComponent {
                                start = game.engine.getEntityByName("Kai")?.getComponents(Components.transform)?.pos!!
                                target = game.engine.getEntityByName("kai_writ_toss_well")?.getComponents(Components.transform)?.pos!!
                                callback = {
                                    val dialog = Node().apply {
                                        color = Color.valueOf("#ff6262")
                                        speaker = "Kai"
                                        type = Node.Type.Text
                                        text = "s1_dlg_kai_torn_to_pieces"
                                    }
                                    val handle = game.showDialog(dialog)

                                    game.engine.timeout(2.5f) {
                                        handle.cancel()
                                        game.blockInteraction = false


                                    }

                                    game.engine.timeout(2.0f) {

                                        PathPlanningComponent {
                                            start = game.engine.getEntityByName("Kai")?.getComponents(Components.transform)?.pos!!
                                            target = game.engine.getEntityByName("kai_waypoint_1")?.getComponents(Components.transform)?.pos!!
                                            callback = {

                                                val waypoints = arrayOf(

                                                        game.engine.getEntityByName("out_main_chars")?.getComponents(Components.transform)?.pos

                                                ).filterNotNull()

                                                game.engine.editEntity("Kai") {
                                                    MoveComponent {
                                                        targetPositions.addAll(waypoints)
                                                        callback = {
                                                            game.blockSaving = false
                                                            game.engine.soundSystem.playMusic("Town of the Fisherman", true, 0.1f);
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


}