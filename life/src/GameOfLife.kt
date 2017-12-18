package kproc.life

import processing.core.PApplet

class GameOfLife() : PApplet() {
    override fun settings() {
        size(20*10, 20*10)
    }

    lateinit var cells: List<Int>

    override fun setup() {
        frameRate(1f)
        background(255)

        // Seed life with chance=0.5
        cells = IntArray(20*20, { if (random(1f) > 0.5) 1 else 0 }).toList()

        drawGrid()
        drawCells()
    }

    override fun draw() {
        background(255)

        drawGrid()
        drawCells()

        val neighbors = cells.mapIndexed {
            i,_ -> neighboring_indices(i).count{ cells[it] == 1 }
        }

        cells = cells.zip(neighbors).map {
            (m,n) -> when (m) {
                         0    -> if (n == 3)    1 else 0  // give birth if exactly 3 neighbors
                         else -> if (n in 2..3) 1 else 0  // stay alive if 2 or 3 neighbors
                     }
        }
    }

    fun neighboring_indices(i: Int): List<Int> {
        val (x,y) = XY(i)
        return listOf(
            I(wdec(x), wdec(y)), I(x, wdec(y)), I(winc(x), wdec(y)),
            I(wdec(x), y),                      I(winc(x), y),
            I(wdec(x), winc(y)), I(x, winc(y)), I(winc(x), winc(y)))
    }

    fun winc(i: Int) = (i+1) % 20
    fun wdec(i: Int) = ((i-1) + 20) % 20
    fun XY(i: Int) = Pair(i % 20, i / 20)
    fun I(x: Int, y: Int) = y*20 + x

    fun drawGrid() {
        for (i in 0..19) {
            line(i*10, 0, i*10, height)
            line(0, i*10, width, i*10)
        }
    }

    fun drawCells() {
        fill(0)
        for (i in cells.indices) {
            if (cells[i] != 0) {
                val (x,y) = XY(i)
                rect(x*10, y*10, 10, 10)
            }
        }
    }
}

fun main(args: Array<String>) {
    PApplet.main("kproc.life.GameOfLife")
}
