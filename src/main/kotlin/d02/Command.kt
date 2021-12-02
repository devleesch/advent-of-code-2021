package d02

abstract class Command(val value: Int) {

    abstract fun apply(position: Position)
    abstract fun apply2(position: Position)

    companion object {
        fun of(type: String, value: Int): Command {
            return when (type) {
                "up" -> Up(value)
                "down" -> Down(value)
                "forward" -> Forward(value)
                else -> throw Exception("Unknown command")
            }
        }
    }

}

class Up(value: Int) : Command(value) {
    override fun apply(position: Position) {
        position.y -= this.value
    }

    override fun apply2(position: Position) {
        position.aim -= this.value
    }
}

class Down(value: Int) : Command(value) {
    override fun apply(position: Position) {
        position.y += this.value
    }

    override fun apply2(position: Position) {
        position.aim += this.value
    }
}

class Forward(value: Int) : Command(value) {
    override fun apply(position: Position) {
        position.x += this.value
    }

    override fun apply2(position: Position) {
        position.x += this.value
        position.y += position.aim * this.value
    }
}