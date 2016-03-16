package de.project.ice.scripting.scripts.scene2

import com.badlogic.ashley.core.Entity
import de.project.ice.IceGame
import de.project.ice.Storage
import de.project.ice.scripting.Script
import de.project.ice.scripting.runOnce
import java.util.*

/**
 * Created by Antoni on 04.03.2016.
 */
class NathanScript: Script() {
    override fun onUpdateEntity(game: IceGame, entity: Entity, delta: Float) {

        if(Storage.SAVESTATE.getString("s2_angelrute")=="true") {
            runOnce("s2_angelrutebekommen") {
                game.inventory.addItem("inv_fishing_rod")
            }
        }
    }
}