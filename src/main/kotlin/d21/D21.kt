package d21

import readLines
import java.lang.Integer.min

fun main() {
    val day = "21"
    println("== Day $day ==")
    val lines = readLines("src/main/resources/d$day/test1.txt", String::class)

    println("part 1: " + part1(lines))
    println("part 2: " + part2(lines))

    println("============")
}

fun part1(lines: List<String>): Any? {
    val player1 = Player(lines[0].last().digitToInt(), 0)
    val player2 = Player(lines[1].last().digitToInt(), 0)

    val dice = Dice(100)
    while(true) {
        player1.move(dice)
//        println("player1: ${player1.points}")
        if (player1.points >= 1000) break

        player2.move(dice)
//        println("player2: ${player2.points}")
        if (player2.points >= 1000) break
    }

    return min(player1.points, player2.points) * dice.turn * 3
}

fun part2(lines: List<String>): Any? {
    val player1 = Player(lines[0].last().digitToInt(), 0)
    val player2 = Player(lines[1].last().digitToInt(), 0)

    val possibilities = mutableListOf<List<Int>>()
    for (i in 3..9) {
        possibilities.addAll(diceFor(i))
    }
    val groupBy = possibilities.groupBy { it.sum() }

    return -1
}

fun diceFor(sum: Int): List<List<Int>> {
    val result = mutableListOf<List<Int>>()
    for (a in 1..3) {
        for (b in 1..3) {
            for (c in 1..3) {
                if (a + b + c == sum) {
                    result.add(listOf(a, b, c))
                }
            }
        }
    }
    return result
}

class Player(var position: Int, var points: Int) {

    fun move(dice: Dice) {
        position += dice.roll()
        while (position > 10) position -= 10
        points += position
    }

}

open class Dice(private val side: Int) {

    var turn = 0
    var currentSide = 1

    open fun roll(): Int {
        val roll = mutableListOf<Int>()

        for (i in 1..3) {
            roll.add(currentSide)
            currentSide++
            if (currentSide > side) {
                currentSide = 1
            }
        }

        turn++

        return roll.sum()
    }

}

class DiracDice : Dice(3) {

    override fun roll(): Int {
        return 0
    }

}




