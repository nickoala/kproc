package kproc.life

import processing.core.PApplet

fun PApplet.line(x1: Number, y1: Number, x2: Number, y2: Number) {
    this.line(x1.toFloat(), y1.toFloat(), x2.toFloat(), y2.toFloat())
}

fun PApplet.rect(a: Number, b: Number, c: Number, d: Number) {
    this.rect(a.toFloat(), b.toFloat(), c.toFloat(), d.toFloat())
}
