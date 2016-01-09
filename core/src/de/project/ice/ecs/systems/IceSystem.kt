package de.project.ice.ecs.systems


import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.EntitySystem
import de.project.ice.ecs.IceEngine

open class IceSystem : EntitySystem {

    constructor() {
    }

    constructor(priority: Int) : super(priority) {
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
        super.removedFromEngine(engine)
    }

    open fun removedFromEngine(engine: IceEngine) {
        super.addedToEngine(engine)
    }

    override fun getEngine(): IceEngine? {
        return super.getEngine() as IceEngine?
    }
}
