package d05

import readLines
import java.lang.Integer.max
import java.lang.Integer.min

fun main() {
    println("== Day 05 ==")
    val lines = readLines("src/main/resources/d05/input.txt", String::class)

    println("part 1: " + part1(lines))
    println("part 2: " + part2(lines))

    println("============")
}

fun part1(lines: List<String>): Int {
    val vents = mutableListOf<Vent>()
    val max = Point(0, 0)
    for (line in lines) {
        val vent = Vent.of(line)

        if (!vent.isDiagonal()) {
            max.x = max(max.x, vent.start.x)
            max.y = max(max.y, vent.start.y)
            max.x = max(max.x, vent.end.x)
            max.y = max(max.y, vent.end.y)

            vents.add(vent)
        }
    }

    val map = MutableList(max.y + 1) { MutableList(max.x + 1) { 0 } }

    for ((i, vent) in vents.withIndex()) {
        for (point in vent.path()) {
            map[point.y][point.x] += 1
        }
    }

    var count = 0
    for (y in map) {
        count += y.count { x -> x > 1 }
    }
    return count
}

fun part2(lines: List<String>): Int {
    val vents = mutableListOf<Vent>()
    val max = Point(0, 0)
    for (line in lines) {
        val vent = Vent.of(line)

        max.x = max(max.x, vent.start.x)
        max.y = max(max.y, vent.start.y)
        max.x = max(max.x, vent.end.x)
        max.y = max(max.y, vent.end.y)

        vents.add(vent)
    }

    val map = MutableList(max.y + 1) { MutableList(max.x + 1) { 0 } }

    for ((i, vent) in vents.withIndex()) {
        for (point in vent.path()) {
            map[point.y][point.x] += 1
        }
    }

    return map.sumOf { y -> y.count { x -> x > 1 } }
}

data class Vent(var start: Point, var end: Point) {

    fun isVertical(): Boolean {
        return this.start.x == this.end.x
    }

    fun isHorizontal(): Boolean {
        return this.start.y == this.end.y
    }

    fun isDiagonal(): Boolean {
        return !(this.isVertical() || this.isHorizontal())
    }

    fun path(): List<Point> {
        val path = mutableListOf<Point>()

        if (this.isVertical()) {
            for (y in min(this.start.y, this.end.y)..max(this.start.y, this.end.y)) {
                path.add(Point(this.start.x, y))
            }
        } else if (this.isHorizontal()) {
            for (x in min(this.start.x, this.end.x)..max(this.start.x, this.end.x)) {
                path.add(Point(x, this.start.y))
            }
        } else {
            var current = Point(this.start)
            val varX = if (this.start.x < this.end.x) 1 else -1
            val varY = if (this.start.y < this.end.y) 1 else -1
            while (current != end) {
                path.add(current)
                current = Point(current.x + varX, current.y + varY)
            }
            path.add(this.end)
        }

        return path
    }

    companion object {
        fun of(str: String): Vent {
            val split = str.split(" -> ")
            return Vent(Point.of(split[0]), Point.of(split[1]))
        }
    }
}

data class Point(var x: Int, var y: Int) {

    constructor(other: Point): this(other.x, other.y)

    operator fun compareTo(other: Point): Int {
        return if (this.x > other.x || this.y > other.y) {
            1
        } else if (this.x == other.x && this.y == this.x) {
            0
        } else {
            -1
        }
    }

    override operator fun equals(other: Any?): Boolean {
        if (other !is Point) return false
        if (this === other) return true
        return this.x == other.x && this.y == other.y
    }

    companion object {
        fun of(str: String): Point {
            val split = str.split(",")
            return Point(split[0].toInt(), split[1].toInt())
        }
    }
}
