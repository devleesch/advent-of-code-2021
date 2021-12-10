package d10

import readLines

fun main() {
    println("== Day 10 ==")
    val lines = readLines("src/main/resources/d10/input.txt", String::class)

    println("part 1: " + part1(lines))
    println("part 2: " + part2(lines))

    println("============")
}

fun part1(lines: List<String>): Any {

    val closeOpens = mapOf(
        ')' to '(',
        ']' to '[',
        '}' to '{',
        '>' to '<'
    )

    val points = mapOf(
        ')' to 3,
        ']' to 57,
        '}' to 1197,
        '>' to 25137
    )

    return lines.map { validate(it, closeOpens) }
        .filterIsInstance<Char>()
        .sumOf { points[it]!! }
}

fun part2(lines: List<String>): Any {
    val openCloses = mapOf(
        '(' to ')',
        '[' to ']',
        '{' to '}',
        '<' to '>'
    )

    val closeOpens = mapOf(
        ')' to '(',
        ']' to '[',
        '}' to '{',
        '>' to '<'
    )

    val points = mapOf(
        ')' to 1,
        ']' to 2,
        '}' to 3,
        '>' to 4
    )

    val sorted = lines.map { validate(it, closeOpens) }
        .filterIsInstance<ArrayDeque<Char>>()
        .filter { it.isNotEmpty() }
        .map {
            var sum: Long = 0
            for (c in it.reversed()) {
                sum *= 5
                sum += points[openCloses[c]]!!
            }
            sum
        }
        .sorted()
    return sorted[sorted.size/2]
}

fun validate(line: String, closeOpens: Map<Char, Char>): Any {
    val stack = ArrayDeque<Char>()
    for (c in line) {
        if (closeOpens.values.contains(c)) {
            stack.add(c)
        } else if (closeOpens.keys.contains(c)) {
            if (stack.removeLast() != closeOpens[c]) {
                return c
            }
        } else {
            return c
        }
    }
    return stack
}
