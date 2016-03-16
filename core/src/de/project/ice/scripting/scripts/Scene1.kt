package de.project.ice.scripting.scripts

import com.badlogic.ashley.core.Entity
import de.project.ice.scripting.Script
import de.project.ice.scripting.runOnce

/**
 * Created by rftpool24 on 15.03.2016.
 */
class Scene1 : Script() {

    override fun onUpdateEntity(entity: Entity, delta: Float) {

        runOnce("scene1_dlg_intro"){
            Game().showDialog("s1_dlg_intro")
        }

    }
    
    override fun onAttachedToEntity(entity: Entity) {
        super.onAttachedToEntity(entity)
    }
}