package de.project.ice.utils

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

infix operator fun Vector2.plus(b: Vector2) = this.cpy().add(b)
infix operator fun Vector2.minus(b: Vector2) = this.cpy().sub(b)
infix operator fun Vector2.times(b: Float) = this.cpy().scl(b)
infix operator fun Vector2.times(b: Int) = this.cpy().scl(b.toFloat())

infix operator fun Vector3.plus(b: Vector3) = this.cpy().add(b)
infix operator fun Vector3.minus(b: Vector3) = this.cpy().sub(b)
infix operator fun Vector3.times(b: Float) = this.cpy().scl(b)
infix operator fun Vector3.times(b: Int) = this.cpy().scl(b.toFloat())