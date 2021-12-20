package d19

import readLines


fun main() {
    val day = "19"
    println("== Day $day ==")
    val lines = readLines("src/main/resources/d$day/input.txt", String::class)

    println("part 1: " + part1(lines))
    println("part 2: " + part2(lines))

    println("============")
}

fun part1(lines: List<String>): Any? {
    val scanners = parse(lines)

    return -1
}

fun part2(lines: List<String>): Any? {
    TODO("Not yet implemented")
}

fun parse(lines: List<String>): List<Scanner> {
    val scanners = mutableListOf<Scanner>()

    val regex = "--- scanner ([0-9]+) ---".toRegex()
    var index = -1
    var beacons = mutableListOf<Beacon>()
    lines.forEach { line ->
        if (regex.matches(line)) {
            beacons = mutableListOf()
            index = regex.find(line)?.groups?.get(1)?.value?.toInt()!!
        } else if(line.isEmpty()) {
            scanners.add(Scanner(index, beacons))
        } else {
            val split = line.split(',')
            beacons.add(Beacon(split[0].toInt(), split[1].toInt(), split[2].toInt()))
        }
    }
    scanners.add(Scanner(index, beacons))

    return scanners
}

class Scanner(val index: Int, beacons: List<Beacon>) {
    val beacons = mutableListOf<List<Beacon>>()
    var orientedBeacons: List<Beacon>? = null

    init {
        this.beacons.addAll(this.rotate(beacons))
    }

    fun rotate(beacons: List<Beacon>): List<List<Beacon>> {
        val rotations = mutableListOf<MutableList<Beacon>>()
        for (p in listOf("xy", "yz", "xz")) {
            for (x in listOf(-1, 1)) {
                for (y in listOf(-1, 1)) {
                    for (z in listOf(-1, 1)) {
                        println("$p -> $x;$y;$z")
                        val rotation = mutableListOf<Beacon>()
                        for (beacon in beacons) {
                            when (p) {
                                "xy" -> rotation.add(Beacon(beacon.y * y, beacon.x * x, beacon.z * z))
                                "yz" -> rotation.add(Beacon(beacon.x * x, beacon.z * z, beacon.y * y))
                                "xz" -> rotation.add(Beacon(beacon.z * z, beacon.y * y, beacon.x * x))
                                else -> throw Exception("unknow rotation")
                            }
                        }
                        rotations.add(rotation)
                    }
                }
            }
        }
        return rotations
    }
}

class Beacon(val x: Int, val y: Int, val z: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Beacon

        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + z
        return result
    }
}
