package de.project.ice.ecs.systems

abstract class IntervalIceSystem(private val interval: Float, priority: Int = 0) : IceSystem(priority) {
    private var accumulator = 0f

    override fun update(deltaTime: Float) {
        accumulator += deltaTime

        while (accumulator >= interval) {
            accumulator -= interval
            updateInterval()
        }
    }

    protected abstract fun updateInterval()
}
