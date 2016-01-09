package de.project.ice.editor

import com.badlogic.ashley.core.Entity
import de.project.ice.ecs.IceEngine

internal class EntityEntry(var entity: Entity) {
    var name: String = generateName(entity)

    override fun toString(): String {
        return name
    }

    companion object {

        fun generateName(entity: Entity): String {
            return IceEngine.getEntityName(entity)
        }
    }
}
