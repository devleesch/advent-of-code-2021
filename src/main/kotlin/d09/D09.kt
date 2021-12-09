package d09

import readLines

fun main() {
    println("== Day 08 ==")
    val lines = readLines("src/main/resources/d09/input.txt", String::class)

    println("part 1: " + part1(lines))
    println("part 2: " + part2(lines))

    println("============")
}

fun part1(lines: List<String>): Any? {
    val heightMap = MutableList(lines.size) { MutableList(lines[0].length) { Cell(-1, -1, -1) } }

    lines.forEachIndexed {
            y, line -> line.forEachIndexed {
                x, c -> heightMap[y][x] = Cell(x, y, c.digitToInt())
            }
    }

    return heightMap.flatMap { l -> l.toList() }
        .filter { cell -> cell.adjacents(heightMap).all { it.height > cell.height } }
        .sumOf { it.height + 1 }
}

fun part2(lines: List<String>): Any? {
    TODO("Not yet implemented")
}

data class Cell(val x: Int, val y: Int, val height: Int) {

    fun left(heightMap: MutableList<MutableList<Cell>>): Cell? {
        return if (this.x - 1 >= 0) {
            heightMap[this.y][this.x - 1]
        } else {
            null
        }
    }

    fun right(heightMap: MutableList<MutableList<Cell>>): Cell? {
        return if (this.x + 1 < heightMap[this.y].size) {
            heightMap[this.y][this.x + 1]
        } else {
            null
        }
    }

    fun top(heightMap: MutableList<MutableList<Cell>>): Cell? {
        return if (this.y - 1 >= 0) {
            heightMap[this.y - 1][this.x]
        } else {
            null
        }
    }

    fun bottom(heightMap: MutableList<MutableList<Cell>>): Cell? {
        return if (this.y + 1 < heightMap.size) {
            heightMap[this.y + 1][this.x]
        } else {
            null
        }
    }

    fun adjacents(heightMap: MutableList<MutableList<Cell>>): List<Cell> {
        return listOfNotNull(left(heightMap), right(heightMap), top(heightMap), bottom(heightMap))
    }

}
