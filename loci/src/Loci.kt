package kproc.loci

import processing.core.PApplet
import processing.core.PVector

class Loci() : PApplet() {
    override fun settings() {
        size(640, 590)
    }

    override fun setup() {
        frameRate(30f)
        background(255)
    }

    val tipRadius = 50f
    val wheelRadii = listOf(50f, 40f, 30f, 20f, 10f)
    val rails = listOf(65f, 180f, 295f, 410f, 525f)
    var roll = 0f

    override fun draw() {
        val cp = wheelRadii
                     .zip(rails)
                     .map {
                         (wheelRadius, rail) ->
                             val center = PVector(roll * wheelRadius, rail)
                             val dot = center + (PVector.fromAngle(roll - HALF_PI) * tipRadius)
                             Pair(center, dot)
                     }

        cp.forEach {
            (_,p) ->
                strokeWeight(1.5f)
                point(p.x, p.y)
        }

        if (cp.count { (c,p) -> c.x < width || p.x < width } == 0) {
            // stop if no circle remains in window
            noLoop()
        }
        else {
            roll += 0.1f
        }
    }
}

fun main(args: Array<String>) {
    PApplet.main("kproc.loci.Loci")
}
