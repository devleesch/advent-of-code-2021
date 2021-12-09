package d09

import readLines

fun main() {
    println("== Day 08 ==")
    val lines = readLines("src/main/resources/d09/input.txt", String::class)

    println("part 1: " + part1(lines))
    println("part 2: " + part2(lines))

    println("============")
}

fun part1(lines: List<String>): Any {
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

fun part2(lines: List<String>): Any {
    val heightMap = MutableList(lines.size) { MutableList(lines[0].length) { Cell(-1, -1, -1) } }

    lines.forEachIndexed {
            y, line -> line.forEachIndexed {
            x, c -> heightMap[y][x] = Cell(x, y, c.digitToInt())
        }
    }

    val cells = heightMap.flatMap { l -> l.toList() }

    val visited = mutableSetOf<Cell>()
    val sizes = mutableListOf<Int>()
    for (cell in cells) {
        val size = explore(cell, heightMap, visited)
        if (size > 0) {
            //println(size)
            sizes.add(size)
        }
    }

    var result = 1
    sizes.sortedDescending().take(3).forEach { result *= it }
    return result
}

fun explore(cell: Cell, heightMap: MutableList<MutableList<Cell>>, visited: MutableSet<Cell>): Int {
    var count = 0
    if (!visited.contains(cell)) {
        visited.add(cell)
        if (cell.height < 9) {
            //println("$cell -> $visited")
            count += 1
            for (adjacent in cell.adjacents(heightMap).minus(visited)) {
                count += explore(adjacent, heightMap, visited)
            }
        }
    }
    return count
}

data class Cell(val x: Int, val y: Int, val height: Int) {

    fun next(x: Int, y: Int, heightMap: MutableList<MutableList<Cell>>): Cell? {
        return if (this.x + x >= 0 && this.x + x < heightMap[0].size && this.y + y >= 0 && this.y + y < heightMap.size) {
            heightMap[this.y + y][this.x + x]
        } else {
            null
        }
    }

    fun left(heightMap: MutableList<MutableList<Cell>>): Cell? {
        return next(0, -1, heightMap)
    }

    fun right(heightMap: MutableList<MutableList<Cell>>): Cell? {
        return next(0, 1, heightMap)
    }

    fun top(heightMap: MutableList<MutableList<Cell>>): Cell? {
        return next(-1, 0, heightMap)
    }

    fun bottom(heightMap: MutableList<MutableList<Cell>>): Cell? {
        return next(1, 0, heightMap)
    }

    fun adjacents(heightMap: MutableList<MutableList<Cell>>): List<Cell> {
        return listOfNotNull(left(heightMap), right(heightMap), top(heightMap), bottom(heightMap))
    }

}
