package d15

import readLines
import java.lang.Integer.min

fun main() {
    val day = "15"
    println("== Day $day ==")
    val lines = readLines("src/main/resources/d$day/input.txt", String::class)

    println("part 1: " + part1(lines))
    println("part 2: " + part2(lines))

    println("============")
}

fun part1(lines: List<String>): Any? {
    val map = getMap(lines, 1)

    val visited = mutableSetOf<Position>()
    map[0][0].distance = 0
    while (!visited.contains(map[map.size - 1][map.size - 1])) {
        val position = map.flatMap { it.toList() }
            .minus(visited)
            .minByOrNull { it.distance }!!
        visited.add(position)

        position.nexts(map)
            .forEach { it.distance = min(position.distance + it.risk, it.distance) }
    }

    return map[map.size - 1][map.size - 1].distance
}

fun part2(lines: List<String>): Any? {
    val map = getMap(lines, 5)

    val visited = mutableSetOf<Position>()
    val nexts = mutableSetOf<Position>()
    map[0][0].distance = 0
    nexts.add(map[0][0])
    while (!visited.contains(map[map.size - 1][map.size - 1])) {
        val position = nexts.minByOrNull { it.distance }!!
        visited.add(position)
        nexts.remove(position)

        val n = position.nexts(map)
        n.forEach {
            if (position.distance + it.risk < it.distance) {
                it.distance = position.distance + it.risk
                it.previous = position
            }
        }

        nexts.addAll(n.minus(visited))
    }

    return map[map.size - 1][map.size - 1].distance
}

fun getMap(lines: List<String>, repeat: Int): List<List<Position>> {
    val tile = mutableListOf<MutableList<Position>>()

    lines.forEachIndexed { y, c ->
        val line = mutableListOf<Position>()
        c.forEachIndexed { x, risk ->
            line.add(Position(x, y, risk.digitToInt()))
        }
        tile.add(line)
    }

    val map = mutableListOf<MutableList<Position>>()

    for (y in 0 until tile.size * repeat) {
        val line = mutableListOf<Position>()
        for (x in 0 until tile.size * repeat) {
            val position = tile[y % tile.size][x % tile.size]
            line.add(Position(x, y, getRisk(position.risk, y / tile.size + x / tile.size)))
        }
        map.add(line)
    }

    return map
}

fun getRisk(risk: Int, repeat: Int): Int {
    val newRisk = risk + repeat
    return if (newRisk > 9) newRisk % 9 else newRisk
}

data class Position(val x: Int, val y: Int, var risk: Int) {

    var previous: Position? = null
    var distance: Int = Int.MAX_VALUE

    fun nexts(map: List<List<Position>>): List<Position> {
        val nexts = mutableListOf<Position>()

        //next(-1, 0, map)?.let { nexts.add(it) }
        next(1, 0, map)?.let { nexts.add(it) }

        //next(0, -1, map)?.let { nexts.add(it) }
        next(0, 1, map)?.let { nexts.add(it) }

        return nexts
    }

    private fun next(dx: Int, dy: Int, map: List<List<Position>>): Position? {
        return if (x + dx >= 0 && x + dx < map[0].size
            && y + dy >= 0 && y + dy < map.size) {
            map[y + dy][x + dx]
        } else {
            null
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Position

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }

}
