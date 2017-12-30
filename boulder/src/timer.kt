package kproc.boulder

class Timer() {
    data class Repetition(val interval: Int, val action: () -> Unit) {
        var lastDone: Int? = null
    }

    val repetitions: MutableList<Repetition> = mutableListOf()

    fun repeat(interval: Int, action: () -> Unit) {
        repetitions.add(Repetition(interval, action))
    }

    fun run(currentMilliseconds: Int) {
        fun isOverdue(interval: Int, lastDone: Int?): Boolean {
            return (lastDone == null ||
                    currentMilliseconds - lastDone >= interval)
        }

        repetitions
            .filter { isOverdue(it.interval, it.lastDone) }
            .forEach {
                it.action()
                it.lastDone = currentMilliseconds
            }
    }
}
