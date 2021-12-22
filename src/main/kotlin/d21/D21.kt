package d21

import readLines
import java.lang.Integer.min

fun main() {
    val day = "21"
    println("== Day $day ==")
    val lines = readLines("src/main/resources/d$day/input.txt", String::class)

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

    val universes = 786316482957123

    for (i in 3..9) {
        val diceFor = diceFor(i)
        println("$i => $diceFor = ${diceFor.size}")
    }

    val results = mutableListOf<List<Int>>()
    for (i in 3..9) {
        results.add(mutableListOf(i))
    }
    val toWin = toWin(0, 4, mutableListOf())

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

fun toWin(points: Int, position: Int, result: MutableList<MutableList<Int>>): MutableList<MutableList<Int>> {
    val newResult = mutableListOf<MutableList<Int>>()
    if (points < 21) {
        for (i in 3..9) {
            newResult.addAll(toWin(points + i, position + i))
        }
    }
    return newResult
}

class Player(var position: Int, var points: Int) {

    fun move(dice: Dice) {
        for (roll in dice.roll()) {
            position += roll
            while (position > 10) position -= 10
        }
        points += position
    }

}

open class Dice(val side: Int) {

    var turn = 0

    open fun roll(): List<Int> {
        val roll = mutableListOf<Int>()

        for (i in 1..3) {
            var value = (turn * 3 + i)
            if (value > side) value %= side
            roll.add(value)
        }

        turn++

        return roll
    }

}


