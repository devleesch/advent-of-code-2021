package d18

import readLines
import kotlin.math.ceil

fun main() {
    val day = "18"
    println("== Day $day ==")
    val lines = readLines("src/main/resources/d$day/input.txt", String::class)

    println("part 1: " + part1(lines))
    println("part 2: " + part2(lines))

    println("============")
}

fun part1(lines: List<String>): Any? {
    return addAll(lines).magnitude()
}

fun part2(lines: List<String>): Any? {
    var max = -1
    var maxN = listOf<String>()

    for (a in lines) {
        for (b in lines) {
            val na = SnailFishNumber.of(a, null)
            val nb = SnailFishNumber.of(b, null)
            if (a != b) {
                val result = na.add(nb)
                var reduce = true
                while (reduce) {
                    reduce = result.reduce(getValues(result), "explode") || result.reduce(getValues(result), "split")
                }

                /*println()
                println("  $a")
                println("+ $b")
                println("= $result -> ${result.magnitude()}")*/

                if (result.magnitude() > max) {
                    max = result.magnitude()
                    maxN = mutableListOf(a, b, result.toString())
                }
            }
        }
    }

    /*println()
    println("  ${maxN[0]}")
    println("+ ${maxN[1]}")
    println("= ${maxN[2]}")*/
    return max
}

fun addAll(lines: List<String>): SnailFishNumber {
    var result = SnailFishNumber.of(lines[0], null)

    for (line in lines.minus(lines[0])) {
        result = result.add(SnailFishNumber.of(line, null))
        var reduce = true
        while (reduce) {
            reduce = result.reduce(getValues(result), "explode") || result.reduce(getValues(result), "split")
        }
    }

    println(result.toString())
    return result
}

fun getValues(n: SnailFishNumber): MutableList<SnailFishNumber> {
    val values = mutableListOf<SnailFishNumber>()
    if (n.value != null) {
        values.add(n)
    } else {
        values.addAll(getValues(n.left!!))
        values.addAll(getValues(n.right!!))
    }
    return values
}

class SnailFishNumber(var parent: SnailFishNumber?) {
    var left: SnailFishNumber? = null
    var right: SnailFishNumber? = null

    var value: Int? = null

    constructor(value: Int, parent: SnailFishNumber?) : this(parent) {
        this.value = value
        this.parent = parent
    }

    fun add(other: SnailFishNumber): SnailFishNumber {
        val sum = SnailFishNumber(null)

        sum.left = this
        sum.left!!.parent = sum

        sum.right = other
        sum.right!!.parent = sum

        return sum
    }

    fun reduce(values: MutableList<SnailFishNumber>, type: String): Boolean {
        val level = level()

        if (type == "explode") {
            if (level >= 4 && this.value == null) {
                explode(values)
                return true
            }
        } else if (type == "split") {
            if (this.value != null && this.value!! > 9) {
                split()
                return true
            }
        }

        if (this.left?.reduce(values, type) == true) {
            return true
        }
        if (this.right?.reduce(values, type) == true) {
            return true
        }
        return false
    }

    fun magnitude(): Int {
        var magnitude = 0

        if (this.value != null) {
            magnitude += this.value!!
        } else {
            magnitude += 3 * this.left?.magnitude()!!
            magnitude += 2 * this.right?.magnitude()!!
        }

        return magnitude
    }

    private fun explode(values: MutableList<SnailFishNumber>) {
        var position = values.indexOf(this.left)
        // previous left
        var left: SnailFishNumber? = null
        if (position > 0) {
            left = values[position - 1]
            left.value = this.left?.value?.let { left.value?.plus(it) }
        }

        // next right
        position = values.indexOf(this.right)
        var right: SnailFishNumber? = null
        if (position < values.size - 1) {
            right = values[position + 1]
            right.value = this.right?.value?.let { right.value?.plus(it) }
        }

        val new = SnailFishNumber(0, parent)
        if (this.parent?.left == this) {
            this.parent!!.left = new
        } else {
            this.parent!!.right = new
        }
    }

    private fun split() {
        val split = SnailFishNumber(parent)
        split.left = SnailFishNumber(this.value!! / 2, split)
        split.right = SnailFishNumber(ceil(this.value!!.toDouble() / 2).toInt(), split)
        if (parent?.left == this) {
            parent!!.left = split
        } else {
            parent!!.right = split
        }
    }

    private fun level(): Int {
        var level = 0
        var parent = this.parent
        while (parent != null) {
            level++
            parent = parent.parent
        }
        return level
    }

    override fun toString(): String {
        return if (left != null && right != null) {
            "[$left,$right]"
        } else {
            "$value"
        }
    }

    companion object {
        fun of(line: String, parent: SnailFishNumber?): SnailFishNumber {
            val line = line.removeSurrounding("[", "]")

            val n: SnailFishNumber = SnailFishNumber(parent)
            if (line.contains("[")) {
                var open = 0
                var middle = 0
                for (c in line) {
                    when (c) {
                        '[' -> open++
                        ']' -> open--
                        ',' -> if (open == 0) break
                        else -> {}
                    }
                    middle++
                }
                n.left = SnailFishNumber.of(line.substring(0, middle), n)
                n.right = SnailFishNumber.of(line.substring(middle + 1), n)
            } else if (line.contains(",")) {
                val split = line.split(',')
                n.left = SnailFishNumber.of(split[0], n)
                n.right = SnailFishNumber.of(split[1], n)
            } else {
                n.value = line.toInt()
            }
            return n
        }
    }
}