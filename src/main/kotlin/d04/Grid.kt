package d04

class Grid {
    var lines = mutableListOf<MutableList<Number>>()

    fun addLine(str: String) {
        val line = mutableListOf<Number>()
        lines.add(line)
        str.split(' ').forEach { v ->
            if (v.isNotEmpty()) {
                line.add(Number(v, false))
            }
        }
    }

    fun apply(number: String) {
        for (line in lines) {
            for (n in line) {
                if (n.value == number) {
                    n.mark = true
                }
            }
        }
    }

    fun isWin(): Boolean {
        for (i in 0 until lines.size) {
            var mark = true
            for (j in 0 until lines[0].size) {
                mark = mark and lines[i][j].mark
            }
            if (mark) return true
        }

        for (j in 0 until lines[0].size) {
            var mark = true
            for (i in 0 until lines.size) {
                mark = mark and lines[i][j].mark
            }
            if (mark) return true
        }

        return false
    }

    fun sumUnmarked(): Int {
        var sum = 0
        for (line in lines) {
            for (number in line) {
                if (!number.mark) {
                    sum += number.value.toInt()
                }
            }
        }
        return sum
    }
}

class Number(val value: String, var mark: Boolean) {}