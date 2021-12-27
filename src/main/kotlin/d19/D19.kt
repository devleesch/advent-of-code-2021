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
    val scanners = parse(lines).toMutableList()

    val scanner0 = scanners.first()
    scanner0.oriented = scanner0.orientations.first().toMutableSet()
    scanners.remove(scanner0)

    while (scanners.isNotEmpty()) {
        val toRemoves = mutableSetOf<Scanner>()
        for (scanner in scanners) {
            for (orientation in scanner.orientations) {
                for (beacon in orientation) {
                    val relatives = orientation.map { it.relativeTo(beacon) }
                    val toAdd = mutableSetOf<Beacon>()
                    for (beacon0 in scanner0.oriented!!) {
                        val relatives0 = scanner0.oriented!!.map { it.relativeTo(beacon0) }
                        if (relatives.intersect(relatives0).size >= 12) {
                            println("$scanner overlap")
                            toAdd.addAll(relatives.map { it.relativeTo(beacon0) })
                            toRemoves.add(scanner)
                        }
                    }
                    scanner0.oriented!!.addAll(toAdd)
                }
            }
        }
        scanners.removeAll(toRemoves)
    }

    return scanner0.oriented!!.size
}

fun part2(lines: List<String>): Any? {
    TODO("Not yet implemented")
}

fun parse(lines: List<String>): List<Scanner> {
    val scanners = mutableListOf<Scanner>()

    val regex = "--- scanner ([0-9]+) ---".toRegex()
    var index = -1
    var beacons = mutableSetOf<Beacon>()
    lines.forEach { line ->
        if (regex.matches(line)) {
            beacons = mutableSetOf()
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

class Scanner(val index: Int, beacons: Set<Beacon>) {
    val orientations = mutableSetOf<Set<Beacon>>()
    var oriented: MutableSet<Beacon>? = null

    init {
        this.orientations.addAll(this.rotate(beacons))
    }

    fun rotate(beacons: Set<Beacon>): Set<Set<Beacon>> {
        val rotations = mutableSetOf<Set<Beacon>>()

        var current = beacons
        for (x in 0 until 4) {
            for (y in 0 until 4) {
                for (z in 0 until 4) {
                    rotations.add(current)
                    current = rotateZ(current)
                }
                current = rotateY(current)
            }
            current = rotateX(current)
        }

        return rotations
    }

    fun rotateX(beacons: Set<Beacon>): Set<Beacon> {
        return beacons.map { Beacon(it.x, it.z, -it.y) }.toSet()
    }

    fun rotateY(beacons: Set<Beacon>): Set<Beacon> {
        return beacons.map { Beacon(it.z, it.y, -it.x) }.toSet()
    }

    fun rotateZ(beacons: Set<Beacon>): Set<Beacon> {
        return beacons.map { Beacon(it.y, -it.x, it.z) }.toSet()
    }

    override fun toString(): String {
        return "Scanner(index=$index)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Scanner

        if (index != other.index) return false
        if (orientations != other.orientations) return false
        if (oriented != other.oriented) return false

        return true
    }

    override fun hashCode(): Int {
        return index
    }


}

data class Beacon(val x: Int, val y: Int, val z: Int) {

    fun relativeTo(other: Beacon): Beacon {
        return Beacon(other.x - x, other.y - y, other.z - z)
    }

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
