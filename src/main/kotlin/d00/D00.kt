package d00

import java.io.File

fun main() {
    File("src/main/resources/d00/input.txt").forEachLine { line -> println(line) }
}