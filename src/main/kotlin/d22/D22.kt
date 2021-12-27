package d22

import readLines
import java.lang.Long.max
import java.lang.Long.min

fun main() {
    val day = "22"
    println("== Day $day ==")
    val lines = readLines("src/main/resources/d$day/test3.txt", String::class)

    println("part 1: " + part1(lines))
    println("part 2: " + part2(lines))

    println("============")
}

fun part1(lines: List<String>): Any? {

    val cubes = mutableMapOf<String, Cube>()
    for (line in lines) {
        println(line)
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

    val xAxis = Axis(mutableListOf())
    val yAxis = Axis(mutableListOf())
    val zAxis = Axis(mutableListOf())

    lines.first().let {
        val intervalsByAxis = parse2(it)
        intervalsByAxis["x"]?.let { it1 -> xAxis.intervals.add(it1) }
        intervalsByAxis["y"]?.let { it1 -> yAxis.intervals.add(it1) }
        intervalsByAxis["z"]?.let { it1 -> zAxis.intervals.add(it1) }
    }

    lines.forEach { line ->
        val intervalsByAxis = parse2(line)

        intervalsByAxis["x"]?.let { xAxis.reduce(it) }
        intervalsByAxis["y"]?.let { yAxis.reduce(it) }
        intervalsByAxis["z"]?.let { zAxis.reduce(it) }
    }

    var sum = 0L
    xAxis.intervals.forEach { x ->
        yAxis.intervals.forEach { y ->
            zAxis.intervals.forEach { z ->
                sum += (x.end - x.start) * (y.end - y.start) * (z.end - z.start)
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

fun parse2(line: String): Map<String, Interval> {
    val regex = "(on|off) x=([\\-0-9]+)..([\\-0-9]+),y=([\\-0-9]+)..([\\-0-9]+),z=([\\-0-9]+)..([\\-0-9]+)".toRegex()
    val find = regex.find(line)

    val intervals = mutableMapOf<String, Interval>()

    find?.let {
        val on = find.groupValues[1] == ("on")
        val x1 = find.groupValues[2].toLong()
        val x2 = find.groupValues[3].toLong()
        val y1 = find.groupValues[4].toLong()
        val y2 = find.groupValues[5].toLong()
        val z1 = find.groupValues[6].toLong()
        val z2 = find.groupValues[7].toLong()

        intervals["x"] = Interval(x1, x2, on)
        intervals["y"] = Interval(y1, y2, on)
        intervals["z"] = Interval(z1, z2, on)
    }

    return intervals
}

class Cube(val x: Int, val y: Int, val z: Int, val state: Boolean){
    fun key(): String {
        return "$x;$y;$z"
    }
}

data class Interval(val start: Long, val end: Long, val state: Boolean) {

    fun apply(other: Interval): List<Interval> {
        val intervals = mutableListOf<Interval>()

        // other start inside this but finish outside
        if (other.start >= this.start && other.start <= this.end && other.end >= this.end) {
            if (other.state) {
                intervals.add(Interval(this.start, other.end, true))
            } else {
                intervals.add(Interval(this.start, other.start, true))
            }
        }
        // other start outside this but end inside this
        else if (other.start <= this.start && other.end >= this.start && other.end <= this.end) {
            if (other.state) {
                intervals.add(Interval(other.start, this.end, true))
            } else {
                intervals.add(Interval(other.end, this.end, true))
            }
        }
        // other start before and end after this
        else if(other.start <= this.start && other.end >= this.end) {
            if (other.state) {
                intervals.add(Interval(other.start, other.end, true))
            }
        }
        // this start before and end after other
        else if (this.start <= this.start && this.end >= other.end) {
            if (other.state) {
                intervals.add(Interval(this.start, this.end, true))
            } else {
                intervals.add(Interval(this.start, other.start, true))
                intervals.add(Interval(other.end, this.end, true))
            }
        }
        // no common part
        else {
            intervals.add(Interval(this.start, this.end, true))
            if (other.state) {
                intervals.add(Interval(other.start, other.end, true))
            }
        }

        return intervals
    }

    fun overlap(other: Interval): Boolean {
        if (this.start <= other.start && this.end >= other.start) return true
        if (this.start >= other.end && this.end <= other.end) return true

        if (other.start <= this.start && other.end >= this.start) return true
        if (other.start >= this.end && other.end <= this.end) return true

        return false
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

data class Axis(var intervals: MutableList<Interval>) {

    fun reduce(interval: Interval) {

        val toReduce = intervals.filter { it.overlap(interval) }

        val newIntervals = intervals.minus(toReduce).toMutableList()

        if (toReduce.isNotEmpty()) {
            if (interval.state) {
                newIntervals.add(Interval(min(toReduce.first().start, interval.start), max(toReduce.last().end, interval.end), true))
            } else {
                if (toReduce.first().start < interval.start && toReduce.last().end <= interval.end) {
                    newIntervals.add(Interval(toReduce.first().start, interval.start - 1, true))
                } else if(toReduce.first().start >= interval.start && toReduce.last().end > interval.end) {
                    newIntervals.add(Interval(interval.end + 1, toReduce.last().end, true))
                } else if (toReduce.first().start < interval.start && toReduce.last().end > interval.end) {
                    newIntervals.add(Interval(toReduce.first().start, interval.start - 1, true))
                    newIntervals.add(Interval(interval.end + 1, toReduce.last().end, true))
                }
            }
        } else {
            if (interval.state) {
                newIntervals.add(interval)
            }
        }

        intervals = newIntervals
        intervals.sortBy { it.start }
    }

}
