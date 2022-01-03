package d24

import readLines
import kotlin.math.pow

fun main() {
    val day = "24"
    println("== Day $day ==")
    val lines = readLines("src/main/resources/d$day/input.txt", String::class)

    println("part 1: " + part1(lines))
    println("part 2: " + part2(lines))

    println("============")
}

fun part1(lines: List<String>): Any? {
    //loop(mutableListOf(), lines)

    re(14, 0, mutableListOf(), mutableMapOf())


    /*for (z in 0..999999) {
        for (i in 1..9) {
            val alu = Alu(i.toLong())
            alu.vars['z'] = z.toLong()
            val lines = readLines("src/main/resources/d24/part14.txt", String::class)
            for (line in lines) {
                alu.exec(line)
            }
            //println("$z -> ${alu.vars}")
            if (alu.vars['z'] == 0L) {
                println("$z -> $i")
            }

        }
    }*/

    return -1
}

fun part2(lines: List<String>): Any? {
    TODO("Not yet implemented")
}

fun loop(list: MutableList<Int>, lines: List<String>) {
    if (list.size == 10) {
        val input = toLong(list.plus(listOf(9, 9, 9, 9)))
        val alu = Alu(input)
        for (line in lines) {
            alu.exec(line)
        }
        println("$input -> ${alu.vars['z']}")
        if (alu.vars['z'] == 0L) {
            readln()
        }
    } else {
        for (i in 1..9) {
            val copy = list.toMutableList()
            copy.add(i)
            loop(copy, lines)
        }
    }
}

fun toLong(list: List<Int>): Long {
    var result = 0L
    list.forEachIndexed { i, v ->
        result += v * 10.toDouble().pow(list.size - 1 - i).toLong()
    }
    return result
}

fun re(part: Int, expected: Long, possibility: List<Long>, memory: MutableMap<String, Long>) {
    println("-> $part - ${possibility.reversed()}")
    if (possibility.size == 14) {
        println("$possibility")
    } else {
        val lines = readLines("src/main/resources/d24/part${part}.txt", String::class)

        var stop = false
        val max = mutableMapOf<Long, Long>()
        for (z in 0..99999L) {
            for (input in 1L..9L) {
                val alu = Alu(input)
                alu.vars['z'] = z
                for (line in lines) {
                    alu.exec(line)
                }
                val value = alu.vars['z']!!

                if (value == expected) {
                    max.computeIfAbsent(input) { z }
                    if (input == 9L) {
                        stop = true
                    }
                }
            }
            if (stop) {
                break
            }
        }

        val maxOf = max.maxOf { entry -> entry.key }
        re(part - 1, max[maxOf]!!, possibility.plus(maxOf), memory)
    }
    //println("<- $part")
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

        when (split[0]) {
            "inp" -> inp(v1)
            "add" -> add(v1, v2)
            "mul" -> mul(v1, v2)
            "div" -> div(v1, v2)
            "mod" -> mod(v1, v2)
            "eql" -> eql(v1, v2)
        }
        //println("$line = $vars")
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
