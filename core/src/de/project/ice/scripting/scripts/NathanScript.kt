package de.project.ice.scripting.scripts

import com.badlogic.ashley.core.Entity
import de.project.ice.Storage
import de.project.ice.scripting.Script
import de.project.ice.scripting.runOnce
import java.util.*

/**
 * Created by Antoni on 04.03.2016.
 */
class NathanScript: Script() {
    override fun onUpdateEntity(entity: Entity, delta: Float) {
        super.onUpdateEntity(entity, delta)

        if(Storage.SAVESTATE.getString("s2_angelrute")=="true") {
            runOnce("s2_angelrutebekommen") {
                Game().inventory.addItem("inv_fishing_rod")
            }
        }
    }
}