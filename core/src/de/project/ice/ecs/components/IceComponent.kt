package de.project.ice.ecs.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool

interface IceComponent : Component, Pool.Poolable

interface CopyableIceComponent : IceComponent {
    fun copyTo(copy: CopyableIceComponent)
}

abstract class IceComponentAdapter : CopyableIceComponent {
    override fun reset() {
    }

    override fun copyTo(copy: CopyableIceComponent) {
    }
}
