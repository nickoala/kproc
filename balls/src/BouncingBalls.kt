package kproc.balls

import processing.core.PApplet
import processing.core.PVector

class Ball(
    position: Position,
    velocity: Velocity,
    val radius: Float,
    val color: Triple<Float, Float, Float>  // RGB
) : Thing(position, velocity)

class BouncingBalls() : PApplet() {
    fun createBall(): Ball {
        return Ball(
                Position(0f, 100f),
                Velocity(PVector.fromAngle(-QUARTER_PI) * random(2f, 10f)),  // random velocity
                random(10f, 30f),                                            // random radius
                Triple(random(0f,255f), random(0f,255f), random(0f,255f)))   // random color
    }

    val balls = mutableListOf(
                    createBall(), createBall(), createBall(), createBall(),
                    createBall(), createBall(), createBall(), createBall())

    val Gravity = Acceleration(0f, 0.5f)

    override fun settings() {
        size(640, 360)
    }

    override fun setup() {
        frameRate(30f)
        ellipseMode(RADIUS)
        background(255)
    }

    override fun draw() {
        background(255)

        balls.forEach {
            it.apply(Gravity)
            it.updatePosition(this::bounce)
        }

        // Remove balls out of screen
        balls.removeAll {
            it.position.x > width + it.radius || it.position.x < -it.radius
        }

        // Create new balls to keep 8 balls in screen
        for (i in balls.size..7) {
            balls.add(createBall())
        }

        balls.forEach {
            draw(it)
        }
    }

    fun bounce(g: Thing) =
        if (g is Ball) {
            // Simulate bouncing: for any balls below the bottom ...
            if (g.position.y > height - g.radius)
                Pair(                      // keep it in screen, and ...
                    Position(g.position.x, (height - g.radius).toFloat()),
                    Velocity(g.velocity.x, g.velocity.y * -0.85f))
                                           // reverse velocity's y-component.
                                           // coefficient of restitution = 0.85
            else
                Pair(g.position, g.velocity)
        }
        else {
            throw IllegalArgumentException()
        }

    fun draw(b: Ball) {
        stroke(b.color, 255)
        fill(b.color, 180)
        circle(b.position, b.radius)
    }
}

fun main(args: Array<String>) {
    PApplet.main("kproc.balls.BouncingBalls")
}
