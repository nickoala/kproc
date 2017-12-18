package kproc.fractal

import processing.core.PApplet
import processing.core.PVector

fun PApplet.quad(p1: PVector, p2: PVector, p3: PVector, p4: PVector) {
    this.quad(
        p1.x, p1.y,
        p2.x, p2.y,
        p3.x, p3.y,
        p4.x, p4.y)
}

fun PApplet.triangle(p1: PVector, p2: PVector, p3: PVector) {
    this.triangle(
        p1.x, p1.y,
        p2.x, p2.y,
        p3.x, p3.y)
}

fun PApplet.dist(p: PVector, q: PVector): Float {
    return PApplet.dist(p.x, p.y, q.x, q.y)
}
