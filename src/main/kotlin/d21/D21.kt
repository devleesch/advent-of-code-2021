package d21

import readLines
import java.lang.Integer.min
import kotlin.math.pow

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

    val repartition = mutableMapOf<Int, Int>()
    for (i in 3..9) {
        val diceFor = diceFor(i)
        //println("$i => $diceFor = ${diceFor.size}")
        repartition[i] = diceFor.size
    }

    val p1 = mutableListOf<MutableList<Move>>()
    toWin(mutableListOf(), player1.position, p1)

    val p2 = mutableListOf<MutableList<Move>>()
    toWin(mutableListOf(), player2.position, p2)

    var previous = System.currentTimeMillis()
    val wins = mutableListOf<Long>(0, 0)
    p1.forEachIndexed { i, a ->
        var result = 1L
        for (move in a) {
            result *= repartition[move.dice]?.toLong()!!
        }
        result *= 27.toDouble().pow(a.size - 1).toLong()
        wins[0] += result

        if (i % 1000 == 0) {
            val current = System.currentTimeMillis()
            println("$i / ${p1.size} - ${current - previous}ms")
            previous = current
        }
    }

    p2.forEachIndexed { i, a ->
        var result = 1L
        for (move in a) {
            result *= repartition[move.dice]?.toLong()!!
        }
        result *= 3.toDouble().pow(a.size).toLong()
        wins[1] += result

        if (i % 1000 == 0) {
            val current = System.currentTimeMillis()
            println("$i / ${p2.size} - ${current - previous}ms")
            previous = current
        }
    }

    println("player1: ${wins[0]} - player2: ${wins[1]}")

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

fun toWin(result: MutableList<Move>, position: Int, results: MutableList<MutableList<Move>>) {
    if (result.sumOf { it.position } >= 21) {
        results.add(result)
    } else {
        for (i in 3..9) {
            val newResult = result.toMutableList()
            var position = getPosition(position, i)
            newResult.add(Move(position, i))
            toWin(newResult, position, results)
        }
    }
}

fun getPosition(start: Int, move: Int): Int {
    var position = start + move
    while (position > 10) position -= 10
    return position
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

class Dice(val side: Int) {

    var turn = 0

    fun roll(): List<Int> {
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

data class Move(val position: Int, val dice: Int)




