package de.project.ice.scripting.scripts

import com.badlogic.ashley.core.Entity
import de.project.ice.scripting.Script
import de.project.ice.scripting.runOnce

/**
 * Created by rftpool24 on 15.03.2016.
 */
class Scene2 : Script() {

    override fun onUpdateEntity(entity: Entity, delta: Float) {

        runOnce("scene2_andi_monologue"){
            Game().showDialog("s2_dlg_andi_monologue")
        }

    }
    
    override fun onAttachedToEntity(entity: Entity) {
        super.onAttachedToEntity(entity)
    }
}