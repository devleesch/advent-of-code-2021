package d22

import readLines
import java.lang.Long.max
import java.lang.Long.min

fun main() {
    val day = "22"
    println("== Day $day ==")
    val lines = readLines("src/main/resources/d$day/input.txt", String::class)

    println("part 1: " + part1(lines))
    println("part 2: " + part2(lines))

    println("============")
}

fun part1(lines: List<String>): Any? {

    val cubes = mutableMapOf<String, Cube>()
    for (line in lines) {
        for (cube in parse1(line)) {
            if (cube.state) {
                cubes.putIfAbsent(cube.key(), cube)
            } else {
                cubes.remove(cube.key())
            }
        }
    }

    return cubes.size
}

fun part2(lines: List<String>): Any? {
    val xAxis = mutableListOf<Float>()
    val yAxis = mutableListOf<Float>()
    val zAxis = mutableListOf<Float>()

    val cubes = mutableListOf<Cube2>()
    lines.forEach {
        val cube = parse2(it)
        cubes.add(cube)

        xAxis.add((cube.x.start - 0.5).toFloat())
        xAxis.add((cube.x.end + 0.5).toFloat())

        yAxis.add((cube.y.start - 0.5).toFloat())
        yAxis.add((cube.y.end + 0.5).toFloat())

        zAxis.add((cube.z.start - 0.5).toFloat())
        zAxis.add((cube.z.end + 0.5).toFloat())
    }

    xAxis.sort()
    yAxis.sort()
    zAxis.sort()

    val on = mutableListOf<MutableList<MutableList<Boolean>>>()
    for (z in 0 until zAxis.size) {
        val zL = mutableListOf<MutableList<Boolean>>()
        for (y in 0 until yAxis.size) {
            val yL = mutableListOf<Boolean>()
            for (x in 0 until xAxis.size) {
                yL.add(false)
            }
            zL.add(yL)
        }
        on.add(zL)
    }


    cubes.map {
        Cube2(
            Interval(xAxis.indexOf((it.x.start - 0.5).toFloat()).toLong(), xAxis.indexOf((it.x.end + 0.5).toFloat()).toLong() - 1),
            Interval(yAxis.indexOf((it.y.start - 0.5).toFloat()).toLong(), yAxis.indexOf((it.y.end + 0.5).toFloat()).toLong() - 1),
            Interval(zAxis.indexOf((it.z.start - 0.5).toFloat()).toLong(), zAxis.indexOf((it.z.end + 0.5).toFloat()).toLong() - 1),
            it.state
        )
    }.forEach {
        for (x in it.x.start..it.x.end) {
            for (y in it.y.start..it.y.end) {
                for (z in it.z.start..it.z.end) {
                    on[z.toInt()][y.toInt()][x.toInt()] = it.state
                }
            }
        }
    }

    var sum = 0L
    on.forEachIndexed { zI, z ->
        z.forEachIndexed { yI, y ->
            y.forEachIndexed { xI, x ->
                if (x) {
                    sum += (xAxis[xI + 1] - xAxis[xI]).toLong() * (yAxis[yI + 1] - yAxis[yI]).toLong() * (zAxis[zI + 1] - zAxis[zI]).toLong()
                }
            }
        }
    }

    return sum
}

fun parse1(line: String): List<Cube> {
    val regex = "(on|off) x=([\\-0-9]+)..([\\-0-9]+),y=([\\-0-9]+)..([\\-0-9]+),z=([\\-0-9]+)..([\\-0-9]+)".toRegex()
    val find = regex.find(line)

    val cubes = mutableListOf<Cube>()
    find?.let {
        val on = find.groupValues[1] == ("on")
        val x1 = find.groupValues[2].toInt()
        val x2 = find.groupValues[3].toInt()
        val y1 = find.groupValues[4].toInt()
        val y2 = find.groupValues[5].toInt()
        val z1 = find.groupValues[6].toInt()
        val z2 = find.groupValues[7].toInt()

        if (x1 >= -50 && x2 <= 50
            && y1 >= -50 && y2 <= 50
            && z1 >= -50 && z2 <= 50) {
            for (x in x1..x2) {
                for (y in y1..y2) {
                    for (z in z1..z2) {
                        cubes.add(Cube(x, y, z, on))
                    }
                }
            }
        }
    }

    return cubes
}

fun parse2(line: String): Cube2 {
    val regex = "(on|off) x=([\\-0-9]+)..([\\-0-9]+),y=([\\-0-9]+)..([\\-0-9]+),z=([\\-0-9]+)..([\\-0-9]+)".toRegex()
    val find = regex.find(line)

    find!!.let {
        val on = find.groupValues[1] == ("on")
        val x1 = find.groupValues[2].toLong()
        val x2 = find.groupValues[3].toLong()
        val y1 = find.groupValues[4].toLong()
        val y2 = find.groupValues[5].toLong()
        val z1 = find.groupValues[6].toLong()
        val z2 = find.groupValues[7].toLong()

        return Cube2(Interval(x1, x2), Interval(y1, y2), Interval(z1, z2), on)
    }
}

class Cube(val x: Int, val y: Int, val z: Int, val state: Boolean) {
    fun key(): String {
        return "$x;$y;$z"
    }
}

data class Cube2(val x: Interval, val y: Interval, val z: Interval, val state: Boolean) {
    fun intersect(other: Cube2): Boolean {
        if (x.intersect(other.x) && y.intersect(other.y) && z.intersect(other.z)) {
            return true
        }
        return false
    }

    fun delta(other: Cube2): Set<Cube2> {
        val cubes = mutableSetOf<Cube2>()
        if (this.intersect(other)) {
            if (other.state) {
                cubes.addAll(this.on(other))
            } else {
                cubes.addAll(this.off(other))
            }
        } else {
            this.state.let { cubes.add(this) }
            other.state.let { cubes.add(other) }
        }
        return cubes
    }

    private fun on(other: Cube2): Set<Cube2> {
        val xDelta = this.x.delta(other.x)
        val yDelta = this.y.delta(other.y)
        val zDelta = this.z.delta(other.z)

        return mutableSetOf(this, other, Cube2(xDelta, yDelta, zDelta, true))
    }

    private fun off(other: Cube2): Set<Cube2> {
        return mutableSetOf()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Cube2

        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false
        if (state != other.state) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        result = 31 * result + state.hashCode()
        return result
    }


}

data class Step(val state: Boolean, val x: Interval, val y: Interval, val z: Interval)

data class Interval(val start: Long, val end: Long) {
    fun intersect(other: Interval): Boolean {
        if (this.start <= other.start && this.end >= other.start) {
            return true
        }

        if (this.start <= other.end && this.end >= other.end) {
            return true
        }

        if (this.start >= other.start && this.end <= other.end) {
            return true
        }

        return false
    }

    fun delta(other: Interval): Interval {
        return Interval(max(this.start, other.start), min(this.end, other.end))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Interval

        if (start != other.start) return false
        if (end != other.end) return false

        return true
    }

    override fun hashCode(): Int {
        var result = start.hashCode()
        result = 31 * result + end.hashCode()
        return result
    }


}