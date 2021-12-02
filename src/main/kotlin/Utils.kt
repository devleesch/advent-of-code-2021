import java.io.File
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
fun <T : Any> readLines(path: String, type: KClass<T>): List<T> {
    return when (type) {
        String::class -> readLinesString(path) as List<T>
        Long::class -> readLinesLong(path) as List<T>
        else -> throw UnsupportedOperationException("Unknown type")
    }
}

private fun readLinesString(path: String): List<String> {
    return File(path).readLines()
}

private fun readLinesLong(path: String): List<Long> {
    val lines = mutableListOf<Long>()
    File(path).forEachLine { line -> lines.add(line.toLong()) }
    return lines
}