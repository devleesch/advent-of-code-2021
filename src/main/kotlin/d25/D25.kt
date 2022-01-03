package d25

import readLines

fun main() {
    val day = "25"
    println("== Day $day ==")
    val lines = readLines("src/main/resources/d$day/input.txt", String::class)

    println("part 1: " + part1(lines))
    println("part 2: " + part2(lines))

    println("============")
}

fun part1(lines: List<String>): Any? {
    val seaFloor = SeaFloor.parse(lines)

    var step = 1
    while (seaFloor.move() > 0) {
        //seaFloor.display()
        //println("==========")
        step++
    }

    return step
}

fun part2(lines: List<String>): Any? {
    TODO("Not yet implemented")
}

class SeaFloor(val seaCucumbers: MutableList<SeaCucumber>, val width: Int, val height: Int) {

    fun move(): Int {
        var nbMoves = 0
        val moves = mutableListOf<Move>()

        seaCucumbers.filter { it.direction == "e" }
            .forEach {
                it.move(width, seaCucumbers)?.let { moves.add(it) }
            }
        moves.forEach { it.apply() }
        nbMoves += moves.size
        moves.clear()

        seaCucumbers.filter { it.direction == "s" }
            .forEach {
                it.move(height, seaCucumbers)?.let { moves.add(it) }
            }
        moves.forEach { it.apply() }
        nbMoves += moves.size

        return nbMoves
    }

    fun display() {
        for (y in 0 until height) {
            for (x in 0 until width) {
                val seaCucumber = seaCucumbers.firstOrNull { it.x == x && it.y == y }
                if (seaCucumber != null) {
                    print(seaCucumber.display())
                } else {
                    print('.')
                }
            }
            println()
        }
    }

    companion object {
        fun parse(lines: List<String>): SeaFloor {
            val seaCucumbers = mutableListOf<SeaCucumber>()
            val width = lines[0].length
            val height = lines.size
            lines.forEachIndexed { y, line ->
                line.forEachIndexed { x, value ->
                    when (value) {
                        '>' -> seaCucumbers.add(SeaCucumber(x, y, "e"))
                        'v' -> seaCucumbers.add(SeaCucumber(x, y, "s"))
                        else -> {}
                    }
                }
            }
            return SeaFloor(seaCucumbers, width, height)
        }
    }
}

class SeaCucumber(var x: Int, var y: Int, val direction: String) {

    fun move(max: Int, seaCucumbers: List<SeaCucumber>): Move? {
        return if (direction == "e") {
            east(max, seaCucumbers)
        } else {
            south(max, seaCucumbers)
        }
    }

    fun east(width: Int, seaCucumbers: List<SeaCucumber>): Move? {
        val newX = if (x + 1 < width) x + 1 else 0
        val first = seaCucumbers.firstOrNull { it.x == newX && it.y == y }
        if (first == null) {
            return Move(this, newX, y)
        }
        return null
    }

    fun south(height: Int, seaCucumbers: List<SeaCucumber>): Move? {
        val newY = if (y + 1 < height) y + 1 else 0
        val first = seaCucumbers.firstOrNull { it.x == x && it.y == newY }
        if (first == null) {
            return Move(this, x, newY)
        }
        return null
    }

    fun display(): String {
        return if (direction == "e") ">" else "v"
    }
}

class Move(val seaCucumber: SeaCucumber, val x: Int, val y: Int) {
    fun apply() {
        seaCucumber.x = x
        seaCucumber.y = y
    }
}