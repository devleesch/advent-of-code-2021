package d13

import readLines

fun main() {
    val day = "13"
    println("== Day $day ==")
    val lines = readLines("src/main/resources/d$day/input.txt", String::class)

    println("part 1: " + part1(lines))
    println("part 2: " + part2(lines))

    println("============")
}

fun part1(lines: List<String>): Any? {
    val dots = mutableListOf<Dot>()
    val folds = mutableListOf<Fold>()

    var isFold = false
    for (line in lines) {
        if (line.isEmpty()) {
            isFold = true
        } else if (isFold) {
            val split = line.split(" ")[2].split("=")
            val axis = split[0][0]
            val position = split[1].toInt()
            folds.add(Fold(axis, position))
        } else {
            val split = line.split(",")
            val x = split[0].toInt()
            val y = split[1].toInt()
            dots.add(Dot(x, y, '#'))
        }
    }

    var manual = Manual(dots, dots.maxOf { it.x } + 1, dots.maxOf { it.y } + 1)
    manual = manual.fold(folds[0])

    return manual.dots.count { it.value == '#' }
}

fun part2(lines: List<String>): Any? {
    val dots = mutableListOf<Dot>()
    val folds = mutableListOf<Fold>()

    var isFold = false
    for (line in lines) {
        if (line.isEmpty()) {
            isFold = true
        } else if (isFold) {
            val split = line.split(" ")[2].split("=")
            val axis = split[0][0]
            val position = split[1].toInt()
            folds.add(Fold(axis, position))
        } else {
            val split = line.split(",")
            val x = split[0].toInt()
            val y = split[1].toInt()
            dots.add(Dot(x, y, '#'))
        }
    }

    var manual = Manual(dots, dots.maxOf { it.x } + 1, dots.maxOf { it.y } + 1)

    for (fold in folds) {
        manual = manual.fold(fold)
    }

    manual.display()
    return "read it :)"
}


data class Fold(val axis: Char, val position: Int)
data class Dot(val x: Int, val y: Int, var value: Char)
class Manual(val dots: MutableList<Dot>, val width: Int, val height: Int) {

    fun apply(otherSide: List<Dot>) {
        for (dot in otherSide) {
            val d = dots.filter { it.x == dot.x }.firstOrNull { it.y == dot.y }
            if (d == null) {
                dots.add(dot)
            }
        }
    }

    fun display() {
        for (y in 0 .. this.height) {
            for (x in 0..this.width) {
                print(dots.filter { it.x == x }.firstOrNull { it.y == y }?.value ?: ' ')
            }
            println()
        }
    }

    fun fold(fold: Fold): Manual {
        return when (fold.axis) {
            'x' -> {
                foldX(fold.position)
            }
            'y' -> {
                foldY(fold.position)
            }
            else -> {
                throw Exception("unknown fold axis: ${fold.axis}")
            }
        }
    }

    private fun foldX(position: Int): Manual {
        val left = dots.filter { it.x < position }.toMutableList()
        val right = dots.filter { it.x > position }
            .map { Dot(this.width - 1 - it.x, it.y, it.value) }

        val manual = Manual(left, width/2, height)
        manual.apply(right)

        return manual
    }

    private fun foldY(position: Int): Manual {
        val up = dots.filter { it.y < position }.toMutableList()
        val down = dots.filter { it.y > position }
            .map { Dot(it.x, this.height - 1 - it.y, it.value) }

        val manual = Manual(up, width, height/2)
        manual.apply(down)

        return manual
    }
}
