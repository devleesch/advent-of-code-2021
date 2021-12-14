package d14

import readLines

fun main() {
    val day = "14"
    println("== Day $day ==")
    val lines = readLines("src/main/resources/d$day/test.txt", String::class)

    println("part 1: " + part1(lines))
    println("part 2: " + part2(lines))

    println("============")
}

fun part1(lines: List<String>): Any? {
    return process(lines, 10)
}

fun part2(lines: List<String>): Any? {
    return process(lines, 40)
}

fun process(lines: List<String>, steps: Int): Any? {
    var template = lines[0]
    var insertion = lines.subList(2, lines.size).associate {
        val split = it.split(" -> ")
        split[0] to split[1]
    }

    for (step in 0 until steps) {
        println("step: $step -> ${template.length}")
        var newTemplate = template[0].toString()
        while (template.isNotEmpty()) {
            val take = template.take(2)
            if (take.length == 2) {
                newTemplate += insertion[take]
                newTemplate += take[1]
            }
            template = template.substring(1)
        }
        template = newTemplate
    }

    val counts = mutableMapOf<Char, Int>()
    template.forEach {
        counts.putIfAbsent(it, 0)
        counts[it] = counts[it]!! + 1
    }

    return counts.values.maxOf { it } - counts.values.minOf { it }
}
