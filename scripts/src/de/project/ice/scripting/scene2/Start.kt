package de.project.ice.scripting.scene2

import com.badlogic.ashley.core.Entity
import de.project.ice.IceGame
import de.project.ice.scripting.Script
import de.project.ice.scripting.runOnce

/**
 * Created by rftpool24 on 15.03.2016.
 */
class Start : Script() {

    override fun onUpdateEntity(game: IceGame, entity: Entity, delta: Float) {

        runOnce("scene2_andi_monologue") {
            game.showDialog("s2_dlg_andi_monologue")
        }

    }
}