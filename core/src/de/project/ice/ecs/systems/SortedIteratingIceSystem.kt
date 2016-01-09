package de.project.ice.ecs.systems


import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.SortedIteratingSystem
import de.project.ice.ecs.IceEngine
import java.util.*

abstract class SortedIteratingIceSystem : SortedIteratingSystem {
    constructor(family: Family, comparator: Comparator<Entity>) : super(family, comparator) {
    }

    constructor(family: Family, comparator: Comparator<Entity>, priority: Int) : super(family, comparator, priority) {
    }

    @Deprecated("", ReplaceWith("addedToEngine(engine as IceEngine)", "de.project.ice.ecs.IceEngine"))
    override fun addedToEngine(engine: Engine) {
        addedToEngine(engine as IceEngine)
    }

    @Deprecated("", ReplaceWith("removedFromEngine(engine as IceEngine)", "de.project.ice.ecs.IceEngine"))
    override fun removedFromEngine(engine: Engine) {
        removedFromEngine(engine as IceEngine)
    }

    open fun addedToEngine(engine: IceEngine) {
        super.addedToEngine(engine)
    }

    fun removedFromEngine(engine: IceEngine) {
        super.removedFromEngine(engine)
    }
}
