# Processing with Kotlin

This project grows out of my desire to learn Kotlin and Processing
simultaneously. It is also an experiment to gain access to Processing's core
library (all those [graphics functions](https://processing.org/reference/))
without using its IDE. As it turned out, Processing and Kotlin can work together
quite seamlessly.

All programs are small and stand-alone. You need **Java 8** and **Gradle 4.4**
to build them:

- `gradle classes` to compile
- `gradle run` to see it

In chronological order:

1. [Bouncing Balls](tree/master/balls): Eight balls bouncing in a window,
   modeling gravity and bouncing.

![Bouncing Balls](tree/master/balls/balls.jpg)

2. [Game of Life](tree/master/life)

![Game of Life](tree/master/life/life.jpg)

3. [Fractals](tree/master/fractal)

![Fractals](tree/master/fractal/fractal.jpg)

4. [Loci](tree/master/loci): Follow a fixed point on a rolling circle. Some
   beautiful curves are traced out.

![Loci](tree/master/loci/loci.jpg)

5. [Cannon Game](tree/master/cannon): Fire cannon balls at a target. Use the
   arrow keys to adjust the shooting angle and firepower, use spacebar to
   launch.

![Cannon Game](tree/master/cannon/cannon.jpg)

Thanks to [The Nature of Code](http://natureofcode.com/book/) for inspirations.
