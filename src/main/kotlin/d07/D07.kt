package d07

import readLines
import java.lang.Integer.min
import java.lang.Math.abs

fun main() {
    println("== Day 07 ==")
    val lines = readLines("src/main/resources/d07/input.txt", String::class)

    println("part 1: " + part1(lines))
    println("part 2: " + part2(lines))

    println("============")
}

fun part1(lines: List<String>): Any? {
    val positions = lines[0].split(',')
        .map { s -> s.toInt() }
        .sorted()

    var minFuel = Int.MAX_VALUE
    for (i in 0..positions.maxOf { p -> p }) {
        var fuel = 0
        for (p in positions) {
            fuel += abs(i - p)
        }
        minFuel = min(minFuel, fuel)
    }

    return minFuel
}

fun part2(lines: List<String>): Any? {
    val positions = lines[0].split(',')
        .map { s -> s.toInt() }
        .sorted()

    var minFuel = Int.MAX_VALUE
    for (i in 0..positions.maxOf { p -> p }) {
        var fuel = 0
        for (p in positions) {
            fuel += fuel(abs(i - p))
        }
        minFuel = min(minFuel, fuel)
    }

    return minFuel
}

fun fuel(move: Int): Int {
    return move * (move + 1) / 2
}

