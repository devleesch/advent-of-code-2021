package d20

import readLines
import kotlin.math.exp
import kotlin.math.max

fun main() {
    val day = "20"
    println("== Day $day ==")
    val lines = readLines("src/main/resources/d$day/input.txt", String::class)

    println("part 1: " + part1(lines))
    println("part 2: " + part2(lines))

    println("============")
}

fun part1(lines: List<String>): Any? {
    return process(lines, 2)
}

fun part2(lines: List<String>): Any? {
    return process(lines, 50)
}

fun process(lines: List<String>, step: Int): Any? {
    val iea = lines[0]

    var minX = 0
    var minY = 0
    var maxX = 0
    var maxY = 0
    var image = mutableSetOf<Pixel>()
    lines.drop(2).forEachIndexed { y, line ->
        maxY = max(maxY, y)
        line.forEachIndexed { x, c ->
            maxX = max(maxX, x)
            if (c == '#') {
                image.add(Pixel(x, y, true))
            }
        }
    }

    val expand = 2
    var default = '.'
    for (step in 0 until step) {
        println("step: $step -> size: ${(maxX - minX) * (maxY - minY)}")

        minX -= expand
        minY -= expand
        maxX += expand
        maxY += expand

        val newImage = mutableSetOf<Pixel>()
        for (y in minY .. maxY) {
            for (x in minX .. maxX) {
                var pixel = Pixel(x, y, true)
                if (!image.contains(pixel)) {
                    pixel = Pixel(x, y, false)
                }
                val range = pixel.range(image, minX + expand, maxX - expand, minY + expand, maxY - expand, default)

                val bin = range.map { if (it.light) '1' else '0' }
                val index = bin.joinToString("").toInt(2)

                //println("$i: $pixel = $index -> ${iea[index]}")

                if (iea[index] == '#') {
                    newImage.add(Pixel(pixel.x, pixel.y, true))
                }
            }
        }

        image = newImage
        //print(minX, maxX, minY, maxY, image)

        minX = image.minOf { it.x }
        minY = image.minOf { it.y }
        maxX = image.maxOf { it.x }
        maxY = image.maxOf { it.y }

        val defaultBin = if (default == '#') 1 else 0
        val defaultIndex = MutableList(9) { defaultBin }.joinToString("").toInt(2)
        default = iea[defaultIndex]
    }

    return image.size
}

fun print(minX: Int, maxX: Int, minY: Int, maxY: Int, image: MutableSet<Pixel>) {
    for (y in minY..maxY) {
        for (x in minX..maxX) {
            val pixel = image.filter { it.y == y }.firstOrNull { it.x == x }
            print(if (pixel != null) '#' else '.')
        }
        println()
    }
}

data class Pixel(val x: Int, val y: Int, var light: Boolean) {

    fun range(image: MutableSet<Pixel>, minX: Int, maxX: Int, minY: Int, maxY: Int, default: Char): MutableList<Pixel> {
        val range = mutableListOf<Pixel>()

        for (dy in listOf(-1, 0, 1)) {
            for (dx in listOf(-1, 0, 1)) {
                if (this.x + dx in minX..maxX && this.y + dy in minY..maxY) {
                    val pixel = image.filter { this.x + dx == it.x }.firstOrNull { this.y + dy == it.y }
                    if (pixel != null) {
                        range.add(pixel)
                    } else {
                        range.add(Pixel(this.x + dx, this.y + dy, false))
                    }
                } else {
                    range.add(Pixel(this.x + dx, this.y + dy, default == '#'))
                }
            }
        }
        return range
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Pixel

        if (x != other.x) return false
        if (y != other.y) return false
        if (light != other.light) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + light.hashCode()
        return result
    }
}
