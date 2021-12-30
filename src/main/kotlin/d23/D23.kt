package d23

import readLines

fun main() {
    val day = "23"
    println("== Day $day ==")

    //println("part 1: " + part1())
    println("part 2: " + part2())

    println("============")
}

fun part1(): Any? {
    val lines = readLines("src/main/resources/d23/input.txt", String::class)

    val game = Game.parse(lines)
    val min = game.min(Int.MAX_VALUE)

    return min
}

fun part2(): Any? {
    val lines = readLines("src/main/resources/d23/input2.txt", String::class)

    val game = Game.parse(lines)
    val min = game.min(Int.MAX_VALUE)

    return min
}

class Game(val positions: List<List<Position>>, val amphipods: List<Amphipod>) {

    var rooms = mutableSetOf<Position>()
    var moves = mutableListOf<Move>()
    var over = false

    init {
        positions[1][3].canStop = false
        positions[1][5].canStop = false
        positions[1][7].canStop = false
        positions[1][9].canStop = false

        for (y in 2 until 2 + amphipods.size / 4) {
            for (x in listOf(3, 5, 7, 9)) {
                rooms.add(positions[y][x])
                when (x) {
                    3 -> positions[y][x].room = 'A'
                    5 -> positions[y][x].room = 'B'
                    7 -> positions[y][x].room = 'C'
                    9 -> positions[y][x].room = 'D'
                    else -> throw Exception("unknown room")
                }
            }
        }
    }

    fun display() {
        positions.forEach { y ->
            y.forEach { x ->
                x.display()
            }
            println()
        }
    }

    fun min(min: Int): Int {
        var min = min
        val moves = amphipods.map { it.moves(this, it.position, 1, mutableSetOf()) }.flatMap { it.toList() }
        if (moves.isNotEmpty()) {
            for (move in moves) {
                val game = this.copy()

                val amphipod = game.amphipods.first { it == move.amphipod }
                val from = game.positions.flatMap { it.toList() }.first { it == move.from }
                val to = game.positions.flatMap { it.toList() }.first { it == move.to }
                val m = Move(amphipod, from, to, move.energy)
                game.moves.add(m)
                m.apply(game)

                //game.display()
                if (!game.over && game.moves.sumOf { it.energy } < min) {
                    min = game.min(min)
                }
            }
        } else {
            if (this.isWin()) {
                val energy = this.moves.sumOf { it.energy }
                if (energy < min) {
                    print("min = $energy")
                    //print(", moves = ${this.moves}")
                    println()
                    min = energy
                }
            }
        }
        return min
    }

    fun isWin(): Boolean {
        var win = true
        for (position in rooms) {
            win = win && position.amphipod != null && position.isRoomFor(position.amphipod!!)
        }
        return win
    }

    fun copy(): Game {
        val positions = mutableListOf<MutableList<Position>>()
        this.positions.forEach { y ->
            val line = mutableListOf<Position>()
            y.forEach { x ->
                line.add(x.copy())
            }
            positions.add(line)
        }

        val amphipods = mutableListOf<Amphipod>()
        this.amphipods.forEach {
            val position = positions[it.position.y][it.position.x]
            val amphipod = it.copy(position)
            position.amphipod = amphipod
            amphipods.add(amphipod)
        }

        val game = Game(positions, amphipods)
        game.moves = this.moves.toMutableList()
        game.over = over
        return game
    }

    companion object {
        fun parse(lines: List<String>): Game {
            val positions = mutableListOf<MutableList<Position>>()
            val amphipods = mutableListOf<Amphipod>()

            lines.forEachIndexed { y, line ->
                val pLine = mutableListOf<Position>()
                line.forEachIndexed { x, c ->
                    val position = Position(x, y)
                    when (c) {
                        '#' -> position.way = false
                        '.' -> position.way = true
                        else -> {
                            val amphipod = Amphipod.of(c, position, amphipods)
                            amphipods.add(amphipod)
                            position.way = true
                            position.amphipod = amphipod
                        }
                    }
                    pLine.add(position)
                }
                positions.add(pLine)
            }

            return Game(positions, amphipods)
        }
    }

}

class Amphipod(val name: String, val energy: Int, var position: Position) {
    var freeze = false

    fun moves(game: Game, position: Position, step: Int, visited: MutableSet<Position>): List<Move> {
        val moves = mutableListOf<Move>()
        if (!freeze) {
            position.nexts(game.positions)
                .minus(visited)
                .forEach {
                    visited.add(it)
                    if (this.canMoveOn(it, game.rooms)) {
                        if (this.canStopOn(it, game.rooms)) {
                            moves.add(Move(this, this.position, it, this.energy * step))
                        }
                        moves.addAll(moves(game, it, step + 1, visited))
                    }
                }
        }
        return moves
    }

    fun canMoveOn(position: Position, rooms: Set<Position>): Boolean {
        if (!position.way) return false
        if (position.amphipod != null) return false
        return true
    }

    fun canStopOn(position: Position, rooms: Set<Position>): Boolean {
        if (!position.canStop) return false
        return if (rooms.contains(this.position)) {
            if (rooms.contains(position)) {
                position.isRoomFor(this)
            } else {
                position.canStop
            }
        } else {
            if (rooms.contains(position)) {
                position.isRoomFor(this)
            } else {
                false
            }
        }
    }

    fun copy(position: Position): Amphipod {
        val amphipod = Amphipod(this.name, this.energy, position)
        amphipod.freeze = this.freeze
        return amphipod
    }

    companion object {
        fun of(type: Char, position: Position, amphipods: List<Amphipod>): Amphipod {
            val i = amphipods.count{ it.name.startsWith(type) }
            return when(type) {
                'A' -> Amphipod("$type$i", 1, position)
                'B' -> Amphipod("$type$i", 10, position)
                'C' -> Amphipod("$type$i", 100, position)
                'D' -> Amphipod("$type$i", 1000, position)
                else -> throw Exception("Unknown type")
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Amphipod

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun toString(): String {
        return "Amphipod(name='$name')"
    }

}

class Position(val x: Int, val y: Int) {
    var amphipod: Amphipod? = null
    var room: Char? = null
    var way = false
    var canStop = true

    fun nexts(positions: List<List<Position>>): List<Position> {
        val nexts = mutableListOf<Position>()
        next(-1, 0, positions)?.let { nexts.add(it) }
        next(1, 0, positions)?.let { nexts.add(it) }
        next(0, -1, positions)?.let { nexts.add(it) }
        next(0, 1, positions)?.let { nexts.add(it) }
        return nexts
    }

    fun next(dx: Int, dy: Int, positions: List<List<Position>>): Position? {
        if (this.x + dx >= 0
            && this.x + dx < positions[0].size
            && this.y + dy >= 0
            && this.y + dy < positions.size
        ) {
            return if (positions[this.y + dy][this.x + dx].way) {
                positions[this.y + dy][this.x + dx]
            } else {
                null
            }
        }
        return null
    }

    fun isRoomFor(amphipod: Amphipod): Boolean {
        return this.room != null && amphipod.name.startsWith(this.room!!)
    }

    fun display() {
        if (amphipod != null) {
            print(amphipod?.name?.get(0).toString())
        } else if (way) {
            print(".")
        } else {
            print("#")
        }
    }

    fun copy(): Position {
        val position = Position(this.x, this.y)
        position.canStop = canStop
        position.room = this.room
        position.way = this.way
        return position
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Position

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }

    override fun toString(): String {
        return "Position(x=$x, y=$y, way=$way, stop=$canStop)"
    }


}

data class Move(var amphipod: Amphipod, var from: Position, var to: Position, var energy: Int) {

    fun apply(game: Game) {
        from.amphipod = null
        to.amphipod = amphipod
        amphipod.position = to
        if (game.rooms.contains(to)) {
            amphipod.freeze = true
            val under = amphipod.position.next(0, 1, game.positions)
            if (under != null) {
                if (under.amphipod == null) {
                    game.over = true
                } else {
                    game.over = !under.isRoomFor(under.amphipod!!)
                }
            }
        }
    }

    override fun toString(): String {
        return "Move(amphipod=${amphipod.name}, from=$from, to=$to, energy=$energy)"
    }
}
