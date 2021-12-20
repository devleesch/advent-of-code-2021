package d20

import readLines
import kotlin.math.max

fun main() {
    val day = "20"
    println("== Day $day ==")
    val lines = readLines("src/main/resources/d$day/test1.txt", String::class)

    println("part 1: " + part1(lines))
    println("part 2: " + part2(lines))

    println("============")
}

fun part1(lines: List<String>): Any? {
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

    val expand = 10
    var default = '.'
    for (step in 0 until 2) {
        var i = 1

        //println(image.size)

        minX -= expand
        minY -= expand
        maxX += expand
        maxY += expand

        val newImage = mutableSetOf<Pixel>()
        for (y in minY .. maxY) {
            for (x in minX .. maxX) {
                var pixel = image.filter { it.x == x }.firstOrNull { it.y == y }
                if (pixel == null) {
                    pixel = Pixel(x, y, false)
                }
                val range = pixel.range(image, minX, maxX, minY, maxY, default)

                val bin = range.map { if (it.light) '1' else '0' }
                val index = bin.joinToString("").toInt(2)

                //println("$i: $pixel = $index -> ${iea[index]}")

                if (iea[index] == '#') {
                    newImage.add(Pixel(pixel.x, pixel.y, true))
                }

                i++
            }
        }

        image = newImage
        print(minX, maxX, minY, maxY, image)

        if (step > 1) {
            val defaultBin = if (default == '#') 1 else 0
            val defaultIndex = MutableList(9) { defaultBin }.joinToString("").toInt(2)
            default = iea[defaultIndex]
        }
    }

    return image.size
}

fun part2(lines: List<String>): Any? {
    TODO("Not yet implemented")
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

}
