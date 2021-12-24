package d24

import readLines

fun main() {
    val day = "24"
    println("== Day $day ==")
    val lines = readLines("src/main/resources/d$day/input.txt", String::class)

    println("part 1: " + part1(lines))
    println("part 2: " + part2(lines))

    println("============")
}

fun part1(lines: List<String>): Any? {

    val input = 91661111111111

    val alu = Alu(input)
    lines.forEach { alu.exec(it) }
    println("$input -> ${alu.vars}")

    return -1
}

fun part2(lines: List<String>): Any? {
    TODO("Not yet implemented")
}

fun generate(value: Long, index: Int): List<Long> {
    val values = mutableListOf<Long>()
    val str = value.toString()
    for (i in 1..9) {
        values.add(str.replaceRange(index, index + 1, i.toString()).toLong())
    }
    return values
}

class Alu(inputs: Long) {

    private var inputs: MutableList<Long> = mutableListOf()

    init {
        this.inputs = inputs.toString().map { it.digitToInt().toLong() }.toMutableList()
    }

    var vars = mutableMapOf(
        'w' to 0L,
        'x' to 0L,
        'y' to 0L,
        'z' to 0L,
    )

    fun exec(line: String) {
        val split = line.split(' ')
        val v1 = split[1][0]

        var v2 = -1L
        if (split.size == 3) {
            v2 = if (split[2].toLongOrNull() == null) vars[split[2][0]]!! else split[2].toLongOrNull()!!
        }

        val save = vars.toMap()
        when (split[0]) {
            "inp" -> inp(v1)
            "add" -> add(v1, v2)
            "mul" -> mul(v1, v2)
            "div" -> div(v1, v2)
            "mod" -> mod(v1, v2)
            "eql" -> eql(v1, v2)
        }
        println("$save -> $line = $vars")
    }

    private fun eql(v1: Char, v2: Long) {
//        println("$v1 = ${vars[v1]} == $v2")
        vars[v1] = if (vars[v1] == v2) 1 else 0
    }

    private fun mod(v1: Char, v2: Long) {
//        println("$v1 = ${vars[v1]} % $v2")
        vars[v1] = vars[v1]?.rem(v2)!!
    }

    private fun div(v1: Char, v2: Long) {
//        println("$v1 = ${vars[v1]} / $v2")
        vars[v1] = vars[v1]?.div(v2)!!
    }

    private fun mul(v1: Char, v2: Long) {
//        println("$v1 = ${vars[v1]} * $v2")
        vars[v1] = vars[v1]?.times(v2)!!
    }

    private fun add(v1: Char, v2: Long) {
//        println("$v1 = ${vars[v1]} + $v2")
        vars[v1] = vars[v1]?.plus(v2)!!
    }

    fun inp(name: Char) {
        println()
        println("$inputs -> $vars")
        val first = inputs.removeFirst()
//        println("$name = $first")
        vars[name] = first
    }
}
