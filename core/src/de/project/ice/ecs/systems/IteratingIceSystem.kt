package de.project.ice.ecs.systems


import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import de.project.ice.ecs.IceEngine

abstract class IteratingIceSystem : IteratingSystem {
    constructor(family: Family) : super(family) {
    }

    constructor(family: Family, priority: Int) : super(family, priority) {
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

    override fun getEngine(): IceEngine {
        return super.getEngine() as IceEngine
    }
}
