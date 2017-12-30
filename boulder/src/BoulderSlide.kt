package kproc.boulder

import processing.core.PApplet
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.World
import org.jbox2d.dynamics.Body
import org.jbox2d.dynamics.Fixture
import org.jbox2d.collision.shapes.PolygonShape
import org.jbox2d.collision.shapes.CircleShape
import org.jbox2d.collision.shapes.ChainShape

class BoulderSlide : PApplet {
    override fun settings() {
        size(640, 360)
    }

    inner class House {
        val body: Body

        constructor(
            wallCenter: Vec2,
            wallHalfWidth: Float,
            wallHalfHeight: Float,
            roofHalfBase: Float,
            roofHeight: Float
        ) {
            body = world.createStaticBody(wallCenter)

            val wall = PolygonShape()
            wall.setAsBox(wallHalfWidth, wallHalfHeight)
            body.createFixture(wall, density=1f, friction=1f)

            val roof = PolygonShape()
            val vs = arrayOf(
                          Vec2(-roofHalfBase, wallHalfHeight),
                          Vec2(roofHalfBase, wallHalfHeight),
                          Vec2(0f, wallHalfHeight + roofHeight))
            roof.set(vs, vs.size)
            body.createFixture(roof, density=1f, friction=1f)
        }

        val ROOF_COLOR = Triple(255f, 0f, 0f)
        val WALL_COLOR = Triple(14f, 141f, 224f)

        fun draw() {
            var f = body.getFixtureList()
            val shapes: MutableList<PolygonShape> = mutableListOf()

            // Build a list of all shapes
            while (true) {
                val s = f.getShape() as PolygonShape
                shapes.add(s)
                f = f.getNext() ?: break
            }

            val p = body.getPosition()
            val colors = arrayOf(ROOF_COLOR, WALL_COLOR)

            pushMatrix()
            translate(p)
            // Draw each shape with color
            shapes.zip(colors).forEach { (s,c) ->
                fill(c)
                beginShape()
                for (i in 0 until s.getVertexCount()) {
                    vertex(s.getVertex(i))
                }
                endShape(CLOSE)
            }
            popMatrix()
        }
    }

    inner class Terrain {
        val body: Body
        val vertices: Array<out Vec2>

        constructor(vararg vs: Vec2) {
            vertices = vs
            body = world.createStaticBody(Vec2(0f, 0f))

            val s = ChainShape()
            s.createChain(vertices, vertices.size)
            body.createFixture(s, restitution=0.0f)
        }

        val SOIL_COLOR = Triple(155f, 128f, 39f)

        fun draw() {
            fill(SOIL_COLOR)
            beginShape()
            vertex(vertices.get(0).x, height/-2f)  // surround area to color
            vertices.forEach { vertex(it) }
            vertex(vertices.get(vertices.lastIndex).x, height/-2f)  // surround area to color
            endShape()
        }
    }

    inner class Boulder {
        val body: Body

        constructor(
            c0: Vec2, r0: Float,
            c1: Vec2, r1: Float,
            c2: Vec2, r2: Float,
            friction: Float, restitution: Float,
            linearVelocity: Vec2
        ) {
            body = world.createDynamicBody(c0, linearVelocity=linearVelocity)

            val s0 = CircleShape()
            s0.setRadius(r0)
            body.createFixture(s0, density=1f, friction=friction, restitution=restitution)

            val s1 = CircleShape()
            s1.setRadius(r1)
            s1.m_p.set(c1)
            body.createFixture(s1, density=1f, friction=friction, restitution=restitution)

            val s2 = CircleShape()
            s2.setRadius(r2)
            s2.m_p.set(c2)
            body.createFixture(s2, density=1f, friction=friction, restitution=restitution)
        }

        fun draw() {
            // The more friction, the darker.
            // Friction [0,1] -> gray [255,0]
            fun gray(f: Fixture) = 255f + f.getFriction() * -255f
            // The more bouncy, the more transparent.
            // Restitution [0,1] -> alpha [255,0]
            fun alpha(f: Fixture) = 255f + f.getRestitution() * -255f

            val p = body.getPosition()
            val a = body.getAngle()
            var f = body.getFixtureList()

            pushMatrix()
            translate(p)
            rotate(a)
            while (true) {
                val s = f.getShape() as CircleShape
                fill(gray(f), alpha(f))
                circle(s.m_p, s.m_radius)
                f = f.getNext() ?: break
            }
            popMatrix()
        }

        fun destroy() {
            world.destroyBody(body)
        }
    }

    val world: World
    val house: House
    val terrain: Terrain
    val boulders: MutableList<Boulder>
    val timer: Timer

    constructor() : super() {
        world = World(Vec2(0f, -10f))
        world.setWarmStarting(true)
        world.setContinuousPhysics(true)

        house = House(Vec2(-200f, -97f), 20f, 13f, 26f, 16f)
        terrain = Terrain(Vec2(360f, 120f), Vec2(-100f, -110f), Vec2(-360f, -110f))

        boulders = mutableListOf()

        timer = Timer()
        timer.repeat(1000, this::createBoulder)
    }

    val SKY_COLOR = Pair(Triple(192f, 239f, 250f), 100f)  // RGB, alpha

    override fun setup() {
        frameRate(90f)
        background(SKY_COLOR)
        ellipseMode(RADIUS)
    }

    override fun draw() {
        background(SKY_COLOR)

        // Use Box2D's coordinate system
        translate(width/2f, height/2f)
        scale(1f, -1f)

        timer.run(millis())
        world.step(1f/30f, 10, 8)

        house.draw()
        terrain.draw()
        boulders.forEach { it.draw() }

        boulders.filter { isOutOfWindow(it) }.forEach { it.destroy() }
        boulders.removeAll { isOutOfWindow(it) }
    }

    fun createBoulder() {
        val c0 = Vec2(width/2f, height/2f)

        fun someBouldersAreTooClose(): Boolean {
            fun isTooClose(b: Body) = dist(c0, b.getPosition()) < 40

            return (boulders.count { isTooClose(it.body) } > 0)
        }

        if (someBouldersAreTooClose()) return

        val r0 = random(5f, 12f)
        val (c1, r1) = randomAttachSite(r0, random(5f, 12f))
        val (c2, r2) = randomAttachSite(r0, random(5f, 12f))

        boulders.add(
            Boulder(
                c0, r0, c1, r1, c2, r2,
                friction=random(1f), restitution=random(1f),
                linearVelocity=unitVector(
                    random(3f*QUARTER_PI, 3f*HALF_PI)) * 40f))
    }

    fun unitVector(radian: Float): Vec2 {
        return Vec2(cos(radian), sin(radian))
    }

    fun randomAttachSite(r0: Float, r: Float): Pair<Vec2, Float> {
        val d = random(r0, r0+r)
        val angle = random(PI*2f)

        return Pair(unitVector(angle)*d, r)
    }

    fun isOutOfWindow(b: Boulder): Boolean {
        val p = b.body.getPosition()
        return (p.x < width/-2f - 50f || p.x > width/2f + 50f)
    }
}

fun main(args: Array<String>) {
    PApplet.main("kproc.boulder.BoulderSlide")
}
