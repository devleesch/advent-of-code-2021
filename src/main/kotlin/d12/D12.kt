package d12

import readLines

fun main() {
    val day = "12"
    println("== Day $day ==")
    val lines = readLines("src/main/resources/d$day/input.txt", String::class)

    println("part 1: " + part1(lines))
    println("part 2: " + part2(lines))

    println("============")
}

fun part1(lines: List<String>): Any? {
    val caves = mutableMapOf<String, Cave>()

    for (line in lines) {
        val splits = line.split('-')

        // build
        caves.putIfAbsent(splits[0], Cave(splits[0]))
        caves.putIfAbsent(splits[1], Cave(splits[1]))

        // link
        caves[splits[0]]?.adjacents?.add(caves[splits[1]]!!)
        caves[splits[1]]?.adjacents?.add(caves[splits[0]]!!)
    }

    val complete = mutableSetOf<MutableList<Cave>>()

    var paths = mutableSetOf<MutableList<Cave>>()
    paths.add(mutableListOf(caves["start"]!!))
    do {
        val newPaths = mutableSetOf<MutableList<Cave>>()
        for (path in paths) {
            if (path.last() != caves["end"]) {
                val minus = path.filter { it.name[0].isLowerCase() }
                    .toSet()

                for (adjacent in path.last().adjacents.minus(minus)) {
                    val newPath = path.toMutableList()
                    newPath.add(adjacent)
                    if (newPath.last() == caves["end"]) {
                        complete.add(newPath)
                    } else {
                        newPaths.add(newPath)
                    }
                }
            }
        }
        paths = newPaths
    } while (newPaths.size > 0)

    return complete.size
}

fun part2(lines: List<String>): Any? {
    val caves = mutableMapOf<String, Cave>()

    for (line in lines) {
        val splits = line.split('-')

        // build
        caves.putIfAbsent(splits[0], Cave(splits[0]))
        caves.putIfAbsent(splits[1], Cave(splits[1]))

        // link
        caves[splits[0]]?.adjacents?.add(caves[splits[1]]!!)
        caves[splits[1]]?.adjacents?.add(caves[splits[0]]!!)
    }

    val complete = mutableSetOf<MutableList<Cave>>()

    var paths = mutableSetOf<MutableList<Cave>>()
    paths.add(mutableListOf(caves["start"]!!))
    do {
        val newPaths = mutableSetOf<MutableList<Cave>>()
        for (path in paths) {
            if (path.last() != caves["end"]) {
                val minus = path.filter { it.name[0].isLowerCase() }
                    .filter { hadTwiceSmallCave(path) }
                    .toMutableSet()
                minus.add(caves["start"]!!)

                for (adjacent in path.last().adjacents.minus(minus)) {
                    val newPath = path.toMutableList()
                    newPath.add(adjacent)
                    if (newPath.last() == caves["end"]) {
                        complete.add(newPath)
                    } else {
                        newPaths.add(newPath)
                    }
                }
            }
        }
        paths = newPaths
    } while (newPaths.size > 0)

    return complete.size
}

fun hadTwiceSmallCave(path: MutableList<Cave>): Boolean {
    return path.filter { it.name[0].isLowerCase() }
        .maxOf { path.count { cave -> cave == it } } > 2
}

class Cave(val name: String) {
    val adjacents = mutableSetOf<Cave>()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Cave

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}