package kproc.balls

import processing.core.PApplet
import processing.core.PVector

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

fun PApplet.fill(color: Triple<Float, Float, Float>, alpha: Number) {
    this.fill(color.first, color.second, color.third, alpha.toFloat())
}

fun PApplet.stroke(color: Triple<Float, Float, Float>, alpha: Number) {
    this.stroke(color.first, color.second, color.third, alpha.toFloat())
}
