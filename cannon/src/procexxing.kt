package kproc.cannon

import processing.core.PApplet
import processing.core.PVector
import processing.core.PImage

operator fun PVector.plus(v: PVector): PVector {
    return PVector.add(this, v)
}

operator fun PVector.minus(v: PVector): PVector {
    return PVector.sub(this, v)
}

operator fun PVector.times(n: Float): PVector {
    return PVector.mult(this, n)
}

operator fun PVector.div(n: Float): PVector {
    return PVector.div(this, n)
}

fun PApplet.circle(center: PVector, radius: Number) {
    this.ellipse(center.x, center.y, radius.toFloat(), radius.toFloat())
}

fun PApplet.line(a: PVector, b: PVector) {
    this.line(a.x, a.y, b.x, b.y)
}

fun PApplet.line(a: Number, b: Number, c: Number, d: Number) {
    this.line(a.toFloat(), b.toFloat(), c.toFloat(), d.toFloat())
}

fun PApplet.random(a: Number, b: Number): Float {
    return this.random(a.toFloat(), b.toFloat())
}
