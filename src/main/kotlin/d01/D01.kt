package d01

import readLines

fun main() {
    val lines = readLines("src/main/resources/d01/input.txt", Long::class)

    println(part1(lines))
    println(part2(lines))
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
        val sumA = sum(lines.subList(i, i+3))
        val sumB = sum(lines.subList(i+1, i+1+3))
        if (sumB > sumA) {
            count++
        }
        i++
    }

    return count
}

fun sum(lines: List<Long>): Long {
    var sum: Long = 0
    for (l in lines) {
        sum += l
    }
    return sum
}
