package d11

import readLines

fun main() {
    println("== Day 11 ==")
    val lines = readLines("src/main/resources/d11/input.txt", String::class)

    println("part 1: " + part1(lines))
    println("part 2: " + part2(lines))

    println("============")
}

fun part1(lines: List<String>): Long {
    val cavern = mutableListOf<MutableList<Octopus>>()

    lines.forEachIndexed { y, line ->
        val l = mutableListOf<Octopus>()
        line.forEachIndexed { x, e ->
            l.add(Octopus(x, y, e.digitToInt(), cavern))
        }
        cavern.add(l)
    }

    var count: Long = 0
    for (step in 1..100) {
        // 1
        cavern.flatMap { it.toList() }
            .forEach {
                it.energy++
            }

        // 2
        val flashed = mutableSetOf<Octopus>()
        while (cavern.flatMap { it.toList() }.minus(flashed).count { it.energy > 9 } > 0) {
            cavern.flatMap { it.toList() }
                .filter { it.energy > 9 }
                .minus(flashed)
                .forEach {
                    flashed.add(it)
                    it.adjacents().forEach { adjacent -> adjacent.energy++ }
                }
        }

        // 3
        flashed.forEach { it.energy = 0 }

        count += flashed.size

    }
    return count
}

fun part2(lines: List<String>): Any? {
    val cavern = mutableListOf<MutableList<Octopus>>()

    lines.forEachIndexed { y, line ->
        val l = mutableListOf<Octopus>()
        line.forEachIndexed { x, e ->
            l.add(Octopus(x, y, e.digitToInt(), cavern))
        }
        cavern.add(l)
    }

    var step: Long = 0
    while (!cavern.flatMap { it.toList() }.all { it.energy == 0 }) {
        // 1
        cavern.flatMap { it.toList() }
            .forEach {
                it.energy++
            }

        // 2
        val flashed = mutableSetOf<Octopus>()
        while (cavern.flatMap { it.toList() }.minus(flashed).count { it.energy > 9 } > 0) {
            cavern.flatMap { it.toList() }
                .filter { it.energy > 9 }
                .minus(flashed)
                .forEach {
                    flashed.add(it)
                    it.adjacents().forEach { adjacent -> adjacent.energy++ }
                }
        }

        // 3
        flashed.forEach { it.energy = 0 }

        step++
    }
    return step
}

fun display(cavern: MutableList<MutableList<Octopus>>) {
    cavern.forEach {
        it.forEach { o ->
            print(o.energy)
        }
        println()
    }
}

class Octopus(private val x: Int, private val y: Int, var energy: Int, private val cavern: MutableList<MutableList<Octopus>>) {
    fun adjacents(): Set<Octopus> {
        val adjacents = mutableSetOf<Octopus>()
        for (dy in listOf(-1, 0, 1)) {
            for (dx in listOf(-1, 0, 1)) {
                next(dx, dy)?.let { adjacents.add(it) }
            }
        }
        return adjacents.minus(this)
    }

    fun next(dx: Int, dy: Int): Octopus? {
        return if (
            x + dx >= 0 && x + dx < cavern[0].size
            && y + dy >= 0 && y + dy < cavern.size
        ) {
            cavern[y + dy][x + dx]
        } else {
            null
        }
    }
}
