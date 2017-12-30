package kproc.boulder

import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.World
import org.jbox2d.dynamics.BodyDef
import org.jbox2d.dynamics.Body
import org.jbox2d.dynamics.BodyType
import org.jbox2d.dynamics.Fixture
import org.jbox2d.dynamics.FixtureDef
import org.jbox2d.collision.shapes.Shape

fun World.createDynamicBody(
    position: Vec2,
    angle: Float? = null,
    linearVelocity: Vec2? = null,
    angularVelocity: Float? = null
): Body {
    val d = BodyDef()
    d.type = BodyType.DYNAMIC
    d.position.set(position)

    angle?.let { d.angle = it }
    linearVelocity?.let { d.linearVelocity.set(it) }
    angularVelocity?.let { d.angularVelocity = it }

    return this.createBody(d)
}

fun World.createStaticBody(
    position: Vec2,
    angle: Float? = null
): Body {
    val d = BodyDef()
    d.type = BodyType.STATIC
    d.position.set(position)
    angle?.let { d.angle = it }

    return this.createBody(d)
}

fun Body.createFixture(
    shape: Shape,
    density: Float? = null,
    friction: Float? = null,
    restitution: Float? = null
): Fixture {
    val d = FixtureDef()
    d.shape = shape;

    density?.let { d.density = density }
    friction?.let { d.friction = friction }
    restitution?.let { d.restitution = restitution }

    return this.createFixture(d)
}

operator fun Vec2.times(n: Float): Vec2 {
    return this.mul(n)
}
