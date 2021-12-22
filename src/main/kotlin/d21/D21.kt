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
    p1.forEachIndexed { ai, a ->
        for (b in p2) {
            var p = 0
            val all = mutableListOf<Move>()
            if (a.size <= b.size) {
                p = 0
                all.addAll(a)
                all.addAll(b.subList(0, a.size - 1))
            } else {
                p = 1
                all.addAll(b)
                all.addAll(a.subList(0, b.size))
            }

            var result = 1L
            for (v in all) {
                result *= repartition[v.dice]!!
            }
            wins[p] += result
        }

        if (ai % 1000 == 0) {
            val current = System.currentTimeMillis()
            println("$ai / ${p1.size} - ${current - previous}ms")
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




