package d04

import readLines

fun main() {
    println("== Day 04 ==")
    val lines = readLines("src/main/resources/d04/input.txt", String::class)

    println("part 1: " + part1(lines))
    println("part 2: " + part2(lines))

    println("============")
}

fun part1(lines: List<String>): String {
    val numbers = lines[0].split(',')

    val grids = mutableListOf<Grid>()
    var grid: Grid? = null
    for (i in 1 until lines.size) {
        val line = lines[i]

        if (line.isEmpty()) {
            grid = Grid()
            grids.add(grid)
        } else {
            grid?.addLine(line)
        }
    }

    for (i in 0 until numbers.size - 1) {
        val number = numbers[i]
        for (grid in grids) {
            grid.apply(number)
            if (grid.isWin()) {
                return (number.toInt() * grid.sumUnmarked()).toString()
            }
        }
    }

    return "Not found"
}

fun part2(lines: List<String>): String {
    val numbers = lines[0].split(',')

    val grids = mutableListOf<Grid>()
    var grid: Grid? = null
    for (i in 1 until lines.size) {
        val line = lines[i]

        if (line.isEmpty()) {
            grid = Grid()
            grids.add(grid)
        } else {
            grid?.addLine(line)
        }
    }

    val wins = mutableListOf<Grid>()
    var number: String? = null
    val nbGrids = grids.size
    for (i in 0 until numbers.size - 1) {
        number = numbers[i]
        for (grid in grids) {
            grid.apply(number)
        }
        val filter = grids.filter { grid -> grid.isWin() }
        wins.addAll(filter)
        grids.removeAll(filter)
        if (wins.size == nbGrids) {
            break
        }
    }

    return (wins[wins.size - 1].sumUnmarked() * number!!.toInt()).toString()
}
