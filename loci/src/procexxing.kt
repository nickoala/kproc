package kproc.loci

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
