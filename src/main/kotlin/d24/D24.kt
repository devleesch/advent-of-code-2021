package d24

import readLines

fun main() {
    val day = "24"
    println("== Day $day ==")
    val lines = readLines("src/main/resources/d$day/part14.txt", String::class)

    println("part 1: " + part1(lines))
    println("part 2: " + part2(lines))

    println("============")
}

fun part1(lines: List<String>): Any? {
    re(14, 0, mutableListOf(), mutableMapOf())
    return -1
}

fun part2(lines: List<String>): Any? {
    TODO("Not yet implemented")
}

fun re(part: Int, expected: Long, possibility: MutableList<Int>, memory: MutableMap<String, Long>) {
    val lines = readLines("src/main/resources/d24/part${part}.txt", String::class)
    for (z in 0..99) {
        for (input in 1L..9L) {
            val value = memory.computeIfAbsent("$part-$z-$input") {
                val alu = Alu(input)
                alu.vars['z'] = z.toLong()
                lines.forEach { alu.exec(it) }
                alu.vars['z']!!
            }

            if (expected == value) {
                //possibility.add(input.toInt())
                //println("part = $part, i = $input, z = $z -> $value")
                if (part > 1) {
                    re(part - 1, z.toLong(), possibility, memory)
                } else {
                    println("found")
                    //println(possibility)
                }
            }
        }
    }
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
        //println("$save -> $line = $vars")
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
//        println()
//        println("$inputs -> $vars")
        val first = inputs.removeFirst()
//        println("$name = $first")
        vars[name] = first
    }
}
