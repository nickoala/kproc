package kproc.cannon

import processing.core.PVector

class Position(x: Float, y: Float) : PVector(x, y) {
    constructor(v: PVector) : this(v.x, v.y)
}

class Velocity(x: Float, y: Float) : PVector(x, y) {
    constructor(v: PVector) : this(v.x, v.y)
}

class Acceleration(x: Float, y: Float) : PVector(x, y) {
    constructor(v: PVector) : this(v.x, v.y)
}

class Force(x: Float, y: Float) : PVector(x, y) {
    constructor(v: PVector) : this(v.x, v.y)
}

open class Thing(val position: Position, val velocity: Velocity, val mass: Float = 1.0f) {
    fun apply(a: Acceleration) {
        velocity.add(a)
    }

    fun apply(f: Force) {
        velocity.add(f / mass)
    }

    fun updatePosition() {
        position.add(velocity)
    }
}
