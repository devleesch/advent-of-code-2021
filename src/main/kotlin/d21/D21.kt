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

    val possibilities = mutableListOf<List<Int>>()
    for (i in 3..9) {
        possibilities.addAll(diceFor(i))
    }
    val matrix = possibilities.groupBy { it.sum() }

    val scores = mutableMapOf(
        1 to 0L,
        2 to 0L
    )
    play(player1, player2, mutableListOf(), scores, matrix)

    return scores.maxOf { it.value }
}

fun play(player1: Player, player2: Player, dices: MutableList<Int>, scores: MutableMap<Int, Long>, matrix: Map<Int, List<List<Int>>>){
    for (d1 in 3..9) {
        val copyDices = dices.toMutableList()
        copyDices.add(d1)

        val copyPlayer1 = player1.copy()
        copyPlayer1.move(d1)
        if (copyPlayer1.points < 21) {
            for (d2 in 3..9) {
                val copyDices2 = copyDices.toMutableList()
                copyDices2.add(d2)

                val copyPlayer2 = player2.copy()
                copyPlayer2.move(d2)
                if (copyPlayer2.points < 21) {
                    play(copyPlayer1, copyPlayer2, copyDices2, scores, matrix)
                } else {
                    scores[2] = scores[2]!! + nbWins(copyDices2, matrix)
                }
            }
        } else {
            scores[1] = scores[1]!! + nbWins(copyDices, matrix)
        }
    }
}

fun nbWins(dices: List<Int>, matrix: Map<Int, List<List<Int>>>): Long {
    var wins = 1L
    for (dice in dices) {
        wins *= matrix[dice]!!.size
    }
    return wins
    /*return dices.map { matrix[it]!!.size }
        .reduce { acc, size -> acc * size }
        .toLong()*/
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

    fun move(dice: Int) {
        position += dice
        while (position > 10) position -= 10
        points += position
    }

    fun copy(): Player {
        return Player(position, points)
    }

    override fun toString(): String {
        return "Player(position=$position, points=$points)"
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




