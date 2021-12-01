package d00

import java.io.File

fun main() {
    val lines = File("src/main/resources/d00/input.txt").readLines()
    println(part1(lines))
    println(part2(lines))
}

fun part1(lines: List<String>): Long {
    for (l1 in lines) {
        for (l2 in lines) {
            val i1 = l1.toLong()
            val i2 = l2.toLong()

            if (i1 + i2 == 2020L) {
                return i1 * i2
            }
        }
    }
    return 0
}

fun part2(lines: List<String>): Long {
    for (l1 in lines) {
        for (l2 in lines) {
            for (l3 in lines) {
                val i1 = l1.toLong()
                val i2 = l2.toLong()
                val i3 = l3.toLong()

                if (i1 + i2 + i3 == 2020L) {
                    return i1 * i2 * i3
                }
            }
        }
    }
    return 0
}