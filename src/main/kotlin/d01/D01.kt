package d01

import readLines

fun main() {
    println("== Day 01 ==")
    val lines = readLines("src/main/resources/d01/input.txt", Long::class)

    println("part 1: " + part1(lines))
    println("part 2: " + part2(lines))

    println("============")
}

fun part1(lines: List<Long>): Long {
    var count: Long = 0
    var previous: Long? = null
    var current: Long

    for (l in lines) {
        current = l
        if ((previous != null) && (current > previous)) {
            count++
        }
        previous = l
    }

    return count
}

fun part2(lines: List<Long>): Long {
    var count: Long = 0

    var i = 0
    while (i < lines.size - 3) {
        val sumA = lines.subList(i, i+3).sum()
        val sumB = lines.subList(i+1, i+1+3).sum()
        if (sumB > sumA) {
            count++
        }
        i++
    }

    return count
}
