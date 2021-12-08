package d08

import readLines

fun main() {
    println("== Day 08 ==")
    val lines = readLines("src/main/resources/d08/input.txt", String::class)

    println("part 1: " + part1(lines))
    println("part 2: " + part2(lines))

    println("============")
}

fun part1(lines: List<String>): Any? {
    val map = lines.map { line -> line.split(" | ") }
        .map { split -> split[1] }
        .map { digit4 -> digit4.split(' ') }
        .flatMap { list -> list.toList() }
        .groupBy { digit -> digit.length }

    var count = 0

    val keep = listOf(2, 4, 3, 7)
    map.forEach { (key, value) -> count += if (key in keep) value.size else 0 }

    return count
}

fun part2(lines: List<String>): Any? {
    return lines.map { line -> decode(line) }
                .sum()
}

fun decode(line: String): Int {
    val split = line.split(" | ")
    val patterns = split[0].split(' ')
        .map { it.toSet() }
    val digits = split[1].split(' ')
        .map { it.toSet() }

    val founds = mutableMapOf(
        1 to patterns.filter { it.size == 2 }[0],
        4 to patterns.filter { it.size == 4 }[0],
        7 to patterns.filter { it.size == 3 }[0],
        8 to patterns.filter { it.size == 7 }[0]
    )

    founds[9] = patterns.filter { it.size == 6 }.first { it.subtract(founds[4]!!).size == 2 }
    founds[2] = patterns.filter { it.size == 5 }.first { it.subtract(founds[4]!!).size == 3 }
    founds[3] = patterns.filter { it.size == 5 }.first { it.subtract(founds[2]!!).size == 1 }
    founds[5] = patterns.filter { it.size == 5 }.first { it.subtract(founds[2]!!).size == 2 }
    founds[6] = patterns.subtract(founds.values.toSet()).first { it.subtract(founds[5]!!).size == 1 }
    founds[0] = patterns.subtract(founds.values.toSet()).first()

    val converter = mutableMapOf<Set<Char>, Int>()
    for (found in founds) {
        converter[found.value.toSortedSet()] = found.key
    }

    var result = ""
    for (digit in digits) {
        result += converter[digit.toSortedSet()]
    }

    return result.toInt()
}