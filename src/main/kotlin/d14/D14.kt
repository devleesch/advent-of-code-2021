package d14

import readLines

fun main() {
    val day = "14"
    println("== Day $day ==")
    val lines = readLines("src/main/resources/d$day/input.txt", String::class)

    println("part 1: " + part1(lines))
    println("part 2: " + part2(lines))

    println("============")
}

fun part1(lines: List<String>): Any? {
    var template = lines[0]
    var insertion = lines.subList(2, lines.size).associate {
        val split = it.split(" -> ")
        split[0] to split[1]
    }

    for (step in 0 until 10) {
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

fun part2(lines: List<String>): Any? {
    val template = lines[0]
    val converters = lines.subList(2, lines.size).associate {
        val split = it.split(" -> ")
        split[0] to split[1][0]
    }.toMutableMap()

    var counts = mutableMapOf<Char, Long>(
        template.first() to 1,
        template.last() to 1
    )

    var shortcuts = mutableMapOf<String, Map<Char, Long>>()

    val maps = template.windowed(2)
        .map { expand(it, 40, converters, shortcuts) }
        .forEach {
            counts = merge(counts, it)
        }

    return counts.values.maxOf { it } - counts.values.minOf { it }
}

fun expand(source: String, step: Int, converters: MutableMap<String, Char>, shortcuts: MutableMap<String, Map<Char, Long>>): Map<Char, Long> {
    var counts = mutableMapOf<Char, Long>()

    val shortcutKey = "$source-$step"
    if (shortcuts.contains(shortcutKey)) {
        return shortcuts[shortcutKey]!!
    }

    val inserted = converters[source]!!

    counts.putIfAbsent(inserted, 0)
    counts.computeIfPresent(inserted) { _, c -> c + 1 }

    val next = step - 1
    if (next > 0) {
        val left = source[0].toString() + inserted
        counts = merge(counts, expand(left, next, converters, shortcuts))

        val right = inserted + source[1].toString()
        counts = merge(counts, expand(right, next, converters, shortcuts))
    }

    shortcuts[shortcutKey] = counts

    return counts
}

fun merge(a: Map<Char, Long>, b: Map<Char, Long>): MutableMap<Char, Long> {
    val r = mutableMapOf<Char, Long>()

    a.forEach { (k, v) -> r[k] = v }
    b.forEach { (k, v) ->
        r.putIfAbsent(k, 0)
        r.computeIfPresent(k) { _, c -> c + v }
    }

    return r
}
