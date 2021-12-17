package d16

import readLines

fun main() {
    val day = "16"
    println("== Day $day ==")
    val lines = readLines("src/main/resources/d$day/input.txt", String::class)

    println("part 1: " + part1(lines))
    println("part 2: " + part2(lines))

    println("============")
}

fun part1(lines: List<String>): Any? {

    var bin = toBin(lines[0])

    val packets = mutableListOf<Packet>()
    while (bin.isNotEmpty()) {
        val packet = Packet.of(bin)
        packet?.let { packets.add(it) }
        bin = packet?.out ?: ""
    }

    return sum(packets)
}

fun part2(lines: List<String>): Any? {
    var bin = toBin(lines[0])

    val packets = mutableListOf<Packet>()
    while (bin.isNotEmpty()) {
        val packet = Packet.of(bin)
        packet?.let { packets.add(it) }
        bin = packet?.out ?: ""
    }

    return packets[0].value()
}

fun toBin(hex: String): String {
    var bin = ""
    hex.forEach { bin += it.digitToInt(16).toString(2).padStart(4, '0') }
    return bin
}

fun sum(packets: List<Packet>): Int {
    var sum = 0
    packets.forEach {
        sum += it.version
        if (it.packets.isNotEmpty()) {
            sum += sum(it.packets)
        }
    }
    return sum
}

abstract class Packet(var out: String, val version: Int, val type: Int) {
    var bin: String = ""
    var value: Long = -1
    val packets = mutableListOf<Packet>()

    abstract fun value(): Long

    companion object {
        fun of(bin: String): Packet? {
            return if (bin.length > 6 && bin.contains("1")) {
                val version = bin.substring(0, 3).toInt(2)

                when (val type = bin.substring(3, 6).toInt(2)) {
                    4 -> Literal(bin, version, type)
                    else -> Operator(bin, version, type)
                }
            } else {
                null
            }
        }
    }
}

class Literal(bin: String, version: Int, type: Int) : Packet(bin, version, type) {

    init {
        this.bin = this.out.take(6)
        this.out = this.out.substring(6)

        var valueBin = ""
        do {
            val group = this.out.take(5)
            this.out = this.out.substring(5)
            this.bin += group
            valueBin += group.substring(1)
        } while (group.startsWith("1"))
        value = valueBin.toLong(2)
    }

    override fun value(): Long {
        return value
    }
}

class Operator(bin: String, version: Int, type: Int) : Packet(bin, version, type) {
    var lengthTypeId: Int = -1
    var totalLength: Int = -1
    var nbSubPacket: Int = -1

    init {
        this.bin = this.out.take(6)
        this.out = this.out.substring(6)

        this.bin += this.out.take(1)
        lengthTypeId = this.bin.last().digitToInt(2)
        this.out = this.out.substring(1)

        if (lengthTypeId == 0) {
            val totalLengthBin = this.out.substring(0, 15)
            totalLength = totalLengthBin.toInt(2)
            this.bin += totalLengthBin
            this.out = this.out.substring(15)

            var subPacketsBin = this.out.take(totalLength)
            while (subPacketsBin.isNotEmpty()) {
                val packet = Packet.of(subPacketsBin)
                packet?.let {
                    packets.add(it)
                    this.bin += packet.bin
                }
                subPacketsBin = packet?.out ?: ""
            }
            this.out = this.out.substring(totalLength)
        } else {
            val nbSubPacketBin = this.out.take(11)
            nbSubPacket = nbSubPacketBin.toInt(2)
            this.bin += nbSubPacketBin
            this.out = this.out.substring(11)

            for (i in 0 until nbSubPacket) {
                val packet = Packet.of(this.out)
                packet?.let {
                    packets.add(it)
                    this.bin += packet.bin
                    this.out = this.out.substring(packet.bin.length)
                }
            }
        }
    }

    override fun value(): Long {
        return when (type) {
            0 -> packets.sumOf { it.value() }
            1 -> packets.map { it.value() }.reduce { acc, value -> acc * value }
            2 -> packets.minOf { it.value() }
            3 -> packets.maxOf { it.value() }
            5 -> if (packets[0].value() > packets[1].value()) 1 else 0
            6 -> if (packets[0].value() < packets[1].value()) 1 else 0
            7 -> if (packets[0].value() == packets[1].value()) 1 else 0
            else -> throw Exception("Unknown operation")
        }
    }
}

