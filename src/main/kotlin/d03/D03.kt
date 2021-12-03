package d03

import readLines

fun main() {
    println("== Day 03 ==")
    val lines = readLines("src/main/resources/d03/input.txt", String::class)

    println("part 1: " + part1(lines))
    println("part 2: " + part2(lines))

    println("============")
}

fun part1(lines: List<String>): String {
    val binary = mutableMapOf<Int, MutableMap<Int, Int>>()

    for (line in lines) {
        var i = 0
        for (c in line) {
            val map = binary.getOrElse(i) { mutableMapOf() }
            if (!binary.containsKey(i)) {
                binary[i] = map
            }
            map[c.digitToInt()] = map.getOrDefault(c.digitToInt(), 0) + 1
            i++
        }
    }

    var gammaRateBinary = ""
    var epsilonRateBinary = ""
    for (map in binary.values) {
        gammaRateBinary += if (map[0]!! > map[1]!!) "0" else "1"
        epsilonRateBinary += if (map[0]!! < map[1]!!) "0" else "1"
    }

    val gammaRate = gammaRateBinary.toInt(2)
    val epsilonRate = epsilonRateBinary.toInt(2)

    return (gammaRate * epsilonRate).toString()

}

fun part2(lines: List<String>): String {
    var oxygen = lines.toList()
    var co2 = lines.toList()

    val size = lines[0].length - 1

    for (i in 0..size) {
        var groupBy = mutableMapOf( '0' to emptyList<String>(), '1' to emptyList())

        if (oxygen.size > 1) {
            groupBy.putAll(oxygen.groupBy { line -> line[i] })
            oxygen = if (groupBy['1']!!.size >= groupBy['0']!!.size) groupBy['1']!! else groupBy['0']!!
        }

        if (co2.size > 1) {
            groupBy = mutableMapOf('0' to emptyList(), '1' to emptyList())
            groupBy.putAll(co2.groupBy { line -> line[i] })
            co2 = if (groupBy['0']!!.size <= groupBy['1']!!.size) groupBy['0']!! else groupBy['1']!!
        }
    }

    return (oxygen[0].toInt(2) * co2[0].toInt(2)).toString()
}