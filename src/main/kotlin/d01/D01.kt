package d01

import java.io.File

fun main() {
    val lines = File("src/main/resources/d01/input.txt").readLines()

    println(part1(lines))
    println(part2(lines))
}

fun part1(lines: List<String>): String {
    var count = 0
    var previous: Int? = null
    var current: Int

    for (l in lines) {
        current = l.toInt()
        if (previous != null && current > previous) {
            count++
        }
        previous = l.toInt()
    }

    return count.toString()
}

fun part2(lines: List<String>): String {
    var count = 0

    var i = 0
    while (i < lines.size - 3) {
        val sumA = sum(lines.subList(i, i+3))
        val sumB = sum(lines.subList(i+1, i+1+3))
        if (sumB > sumA) {
            count++
        }
        i++
    }

    return count.toString()
}

fun sum(lines: List<String>): Int {
    var sum = 0
    for (l in lines) {
        sum += l.toInt()
    }
    return sum
}
