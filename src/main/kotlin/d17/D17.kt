package d17

import readLines
import java.lang.Integer.max
import java.lang.Integer.min

fun main() {
    val day = "17"
    println("== Day $day ==")
    val lines = readLines("src/main/resources/d$day/input.txt", String::class)

    println("part 1: " + part1(lines))
    println("part 2: " + part2(lines))

    println("============")
}

fun part1(lines: List<String>): Any? {
    val regex = "target area: x=([0-9]+)..([0-9]+), y=([\\-0-9]+)..([\\-0-9]+)".toRegex()

    val matches = regex.find(lines[0])

    val target = Area(
        matches!!.groups[1]!!.value.toInt(),
        matches.groups[2]!!.value.toInt(),
        matches.groups[3]!!.value.toInt(),
        matches.groups[4]!!.value.toInt(),
    )

    var maxY = Int.MIN_VALUE
    for (vx in 0..50) {
        for(vy in -20..20) {
            val probe = Probe(0, 0, vx, vy)
            for (i in 0..1000) {
                probe.step()
                if (probe.isIn(target)) {
                    maxY = max(maxY, probe.maxY)
                    break
                }
            }
        }
    }

    return maxY
}

fun part2(lines: List<String>): Any? {
    val regex = "target area: x=([0-9]+)..([0-9]+), y=([\\-0-9]+)..([\\-0-9]+)".toRegex()

    val matches = regex.find(lines[0])

    val target = Area(
        matches!!.groups[1]!!.value.toInt(),
        matches.groups[2]!!.value.toInt(),
        matches.groups[3]!!.value.toInt(),
        matches.groups[4]!!.value.toInt(),
    )

    val count = mutableListOf<Probe>()
    for (vx in 0..1000) {
        for(vy in -1000..1000) {
            val probe = Probe(0, 0, vx, vy)
            for (i in 0..1000) {
                probe.step()
                if (probe.isIn(target)) {
                    count.add(probe)
                    break
                }

                if (probe.x > target.x2 || probe.y < target.y1) {
                    break
                }
            }
        }
    }

    return count.size
}

class Area(x1: Int, x2: Int, y1: Int, y2: Int) {

    val x1: Int
    val x2: Int
    val y1: Int
    val y2: Int

    init {
        this.x1 = min(x1, x2)
        this.x2 = max(x1, x2)
        this.y1 = min(y1, y2)
        this.y2 = max(y1, y2)
    }
}

data class Probe(var x: Int, var y: Int, var vx: Int, var vy: Int) {

    var maxY = Int.MIN_VALUE

    fun step() {
        x += vx
        y += vy

        maxY = max(maxY, y)

        if (vx > 0) {
            vx -= 1
        } else if (vx < 0) {
            vx += 1
        }

        vy -= 1
    }

    fun isIn(area: Area): Boolean {
        return x >= area.x1 && x <= area.x2 && y >= area.y1 && y <= area.y2
    }

}
