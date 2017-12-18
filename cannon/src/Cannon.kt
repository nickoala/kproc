package kproc.cannon

import processing.core.PApplet
import processing.core.PVector

class Ball(
    position: Position,
    velocity: Velocity,
    val radius: Float
) : Thing(position, velocity)

class Scene {
    var targetDistance = 0f
    var wind = 0f
}

class Cannon {
    var angle = 0f
    var speed = 0f
}

class Crater(val distance: Float)

class Game() : PApplet() {
    override fun settings() {
        size(1200, 640)
    }

    enum class Phase {
        PRE_LAUNCH, LAUNCHED, HIT
    }
    var phase = Phase.HIT

    val Gravity = Acceleration(0f, 0.5f)
    val floor = 590f
    val cannonBallRadius = 6f
    val cannonBallOrigin = PVector(30f, floor - cannonBallRadius)
    val impactRadius = 20f

    val scene = Scene()
    val cannon = Cannon()
    var cannonBall: Ball? = null
    val craters = mutableListOf<Crater>()

    fun reset() {
        cannonBall = null
        craters.clear()

        scene.targetDistance = random(150, width - 50)
        scene.wind = random(-10, 10)
        cannon.angle = QUARTER_PI
        cannon.speed = 15f
    }

    override fun setup() {
        frameRate(30f)
        ellipseMode(RADIUS)
        background(255)
    }

    override fun draw() {
        background(255)

        if (phase == Phase.HIT) {
            reset()
            phase = Phase.PRE_LAUNCH
        }

        if (phase == Phase.PRE_LAUNCH) {
            if (keyPressed) {
                if (key.toInt() == CODED) {
                    when (keyCode) {
                        // restrict ranges
                        UP    -> cannon.speed = min(cannon.speed + 0.1f, 100f)
                        DOWN  -> cannon.speed = max(cannon.speed - 0.1f, 5f)
                        LEFT  -> cannon.angle = min(cannon.angle + radians(0.1f), PI)
                        RIGHT -> cannon.angle = max(cannon.angle - radians(0.1f), 0f)
                    }
                }
                else if (key == ' ') {
                    // Fire!
                    cannonBall = Ball(
                                     Position(cannonBallOrigin),
                                     Velocity(PVector.fromAngle(-1f * cannon.angle) * cannon.speed),
                                     cannonBallRadius)
                    phase = Phase.LAUNCHED
                }
            }
        }

        draw(scene)
        draw(cannon)

        cannonBall?.let {
            it.apply(airResistance(it.velocity, Velocity(scene.wind, 0f)))
            it.apply(Gravity)
            it.updatePosition()

            draw(it)

            if (it.position.y > floor) {
                cannonBall = null

                val dx = (floor - it.position.y) / tan(it.velocity.heading())
                val impact = it.position.x + dx

                if (abs(impact - scene.targetDistance) <= impactRadius) {
                    phase = Phase.HIT
                }
                else {
                    phase = Phase.PRE_LAUNCH
                    craters.add(Crater(impact))
                }
            }
        }

        craters.forEach { draw(it) }
    }

    fun airResistance(vobj: Velocity, vair: Velocity): Force {
        val vnet = vobj - vair
        val c = 0.001f
        val speed = vnet.mag()
        return Force((vnet * -1f).normalize() * speed * speed * c)
    }

    fun draw(b: Ball) {
        fill(0)
        circle(b.position, b.radius)
    }

    fun draw(scene: Scene) {
        stroke(0)
        strokeWeight(1f)
        line(0, floor, width, floor)

        // Draw a flag as target
        var p = PVector(scene.targetDistance, floor)
        for (i in listOf(PVector(0f, -40f), PVector(-16f, 7f), PVector(16f, 7f))) {
            line(p, p+i)
            p = p+i
        }

        // Draw arrow indicating wind
        val middle = PVector(width / 2f, height - 20f)
        val tail = middle - PVector(scene.wind * 5f, 0f)
        val head = middle + PVector(scene.wind * 5f, 0f)

        line(tail, head)
        line(head, head + PVector(if (scene.wind > 0) -5f else 5f, 5f))
        line(head, head + PVector(if (scene.wind > 0) -5f else 5f, -5f))
    }

    fun draw(cannon: Cannon) {
        // Draw a circle as base
        fill(0)
        noStroke()
        circle(cannonBallOrigin, cannonBallRadius)

        // Draw a protruding line
        stroke(0)
        strokeWeight(3f)
        strokeCap(SQUARE)
        val tip = PVector.fromAngle(-1f * cannon.angle) * 20f
        line(cannonBallOrigin, cannonBallOrigin + tip)

        // Draw launch info
        val lines = listOf(
                        "Angle: ${"%.1f".format(degrees(cannon.angle))}",
                        "Speed: ${"%.1f".format(cannon.speed)}")
        text(lines.joinToString("\n"), 10f, height - 25f)
    }

    fun draw(crater: Crater) {
        fill(255)
        stroke(0)
        strokeWeight(1f)
        arc(crater.distance, floor, impactRadius, impactRadius, -0.1f, PI+0.1f, OPEN)
    }
}

fun main(args: Array<String>) {
    PApplet.main("kproc.cannon.Game")
}
