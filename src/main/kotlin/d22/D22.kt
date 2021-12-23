package d22

import readLines
import java.lang.Integer.min
import kotlin.math.max

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
        println(line)
        for (cube in parse(line)) {
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
    TODO("Not yet implemented")
}

fun parse(line: String): List<Cube> {
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

class Cube(val x: Int, val y: Int, val z: Int, val state: Boolean){
    fun key(): String {
        return "$x;$y;$z"
    }
}

class Interval(val start: Int, val end: Int) {

    fun apply(other: Interval): List<Interval> {
        val intervals = mutableListOf<Interval>()

        // other start inside this but finish outside
        if (other.start >= this.start && other.start <= this.end && other.end >= this.end) {
            intervals.add(Interval(this.start, other.end))
        }
        // other start outside this but end inside this
        else if (other.start <= this.start && other.end >= this.start && other.end <= this.end) {
            intervals.add(Interval(other.start, this.end))
        }
        // other start before and end after this
        else if(other.start <= this.start && other.end >= this.end) {
            intervals.add(Interval(other.start, other.end))
        }
        // this start before and end after other
        else if (this.start <= this.start && this.end >= other.end) {
            intervals.add(Interval(this.start, this.end))
        }
        // no common part
        else {
            intervals.add(Interval(this.start, this.end))
            intervals.add(Interval(other.start, other.end))
        }

        return intervals
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
        var result = start
        result = 31 * result + end
        return result
    }


}
