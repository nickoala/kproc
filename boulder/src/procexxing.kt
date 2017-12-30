package kproc.boulder

import processing.core.PApplet
import org.jbox2d.common.Vec2

fun PApplet.translate(t: Vec2) {
    this.translate(t.x, t.y)
}

fun PApplet.vertex(v: Vec2) {
    this.vertex(v.x, v.y)
}

fun PApplet.circle(c: Vec2, r: Float) {
    this.ellipse(c.x, c.y, r, r)
}

fun PApplet.fill(rgb: Triple<Float, Float, Float>) {
    val (r,g,b) = rgb
    this.fill(r, g, b)
}

fun PApplet.background(rgba: Pair<Triple<Float, Float, Float>, Float>) {
    val (rgb,a) = rgba
    val (r,g,b) = rgb
    this.background(r, g, b, a)
}

fun PApplet.dist(a: Vec2, b: Vec2): Float {
    return PApplet.dist(a.x, a.y, b.x, b.y)
}
