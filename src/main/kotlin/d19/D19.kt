package d19

import readLines
import kotlin.math.abs


fun main() {
    val day = "19"
    println("== Day $day ==")
    val lines = readLines("src/main/resources/d$day/input.txt", String::class)

    resolve(lines)

    println("============")
}

fun resolve(lines: List<String>) {
    val scanners = parse(lines).toMutableList()

    val pScanners = mutableSetOf<Scanner>()
    val scanner0 = scanners.first()
    scanner0.oriented = scanner0.orientations.first().toMutableSet()
    scanners.remove(scanner0)
    pScanners.add(scanner0)

    while (scanners.isNotEmpty()) {
        println("${scanners.size} -> $scanners")
        val toRemoves = mutableSetOf<Scanner>()
        for (scanner in scanners) {
            val toAdd = mutableSetOf<Beacon>()
            for (orientation in scanner.orientations) {
                for (beacon in orientation) {
                    val relatives = orientation.map { it.relativeTo(beacon) }
                    for (beacon0 in scanner0.oriented!!) {
                        val relatives0 = scanner0.oriented!!.map { it.relativeTo(beacon0) }
                        if (relatives.intersect(relatives0.toSet()).size >= 12) {
                            toAdd.addAll(relatives.map { it.relativeTo(beacon0) })
                            toRemoves.add(scanner)
                            scanner.x = beacon0.x - beacon.x
                            scanner.y = beacon0.y - beacon.y
                            scanner.z = beacon0.z - beacon.z
                            break
                        }
                    }
                    if (toAdd.size > 0) {
                        break
                    }
                }
                if (toAdd.size > 0) {
                    break
                }
            }
            scanner0.oriented!!.addAll(toAdd)
            pScanners.add(scanner)
        }
        scanners.removeAll(toRemoves)
    }

    println("part1: ${scanner0.oriented!!.size}")

    var maxDistance = Int.MIN_VALUE
    for (a in pScanners) {
        for (b in pScanners) {
            val distance = a.distance(b)
            if (distance > maxDistance) {
                maxDistance = distance
            }
        }
    }
    println("part2: $maxDistance")
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
    var x: Int = 0
    var y: Int = 0
    var z: Int = 0

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

    fun distance(other: Scanner): Int {
        return abs(x - other.x) + abs(y - other.y )+ abs(z - other.z)
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
