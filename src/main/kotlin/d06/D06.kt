package d06

import readLines

fun main() {
    println("== Day 06 ==")
    val lines = readLines("src/main/resources/d06/input.txt", String::class)

    println("part 1: " + part1(lines))
    println("part 2: " + part2(lines))

    println("============")
}

fun part1(lines: List<String>): Any? {
    var fishes = mutableMapOf<Int, Long>()
    init(fishes)

    lines[0].split(',')
            .forEach { timer ->
                fishes[timer.toInt()] = fishes[timer.toInt()]!! + 1
            }

    val newFishes = mutableMapOf<Int, Long>()
    for (i in 1..80) {
        init(newFishes)
        //println("day: $i -> ${fishes.values.sum()}")

        for (entry in fishes) {
            if (entry.key != 0) {
                newFishes[entry.key - 1] = newFishes[entry.key - 1]!! + entry.value
            } else {
                newFishes[6] = newFishes[6]!! + entry.value
                newFishes[8] = entry.value
            }
        }

        fishes = newFishes.toMutableMap()
    }

    return fishes.values.sum()
}

fun part2(lines: List<String>): Any? {
    var fishes = mutableMapOf<Int, Long>()
    init(fishes)

    lines[0].split(',')
        .forEach { timer ->
            fishes[timer.toInt()] = fishes[timer.toInt()]!! + 1
        }

    val newFishes = mutableMapOf<Int, Long>()
    for (i in 1..256) {
        init(newFishes)
        //println("day: $i -> ${fishes.values.sum()}")

        for (entry in fishes) {
            if (entry.key != 0) {
                newFishes[entry.key - 1] = newFishes[entry.key - 1]!! + entry.value
            } else {
                newFishes[6] = newFishes[6]!! + entry.value
                newFishes[8] = entry.value
            }
        }

        fishes = newFishes.toMutableMap()
    }

    return fishes.values.sum()
}

fun init(map: MutableMap<Int, Long>) {
    for (i in 0..8) {
        map[i] = 0
    }
}
