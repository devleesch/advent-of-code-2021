package d02

import readLines

fun main() {

    val lines = readLines("src/main/resources/d02/input.txt", String::class)

    println(part1(lines))
    println(part2(lines))
}

fun part1(lines: List<String>): String {

    val position = Position(0, 0)

    for (line in lines) {
        val split = line.split(" ")
        val type = split[0]
        val value = split[1].toInt()

        val command = Command.of(type, value)
        command.apply(position)
    }

    return (position.x * position.y).toString()

}

fun part2(lines: List<String>): String {
    val position = Position(0, 0)

    for (line in lines) {
        val split = line.split(" ")
        val type = split[0]
        val value = split[1].toInt()

        val command = Command.of(type, value)
        command.apply2(position)
    }

    return (position.x * position.y).toString()
}
