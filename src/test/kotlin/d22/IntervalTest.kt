package d22

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class IntervalTest {

    @Test
    fun no_overlap() {
        val a = Interval(0, 5)
        val b = Interval(10, 12)

        val intervals = a.apply(b)

        assertEquals(2, intervals.size)
        assertEquals(Interval(0, 5), intervals[0])
        assertEquals(Interval(10, 12), intervals[1])
    }

    @Test
    fun overlap_at_start() {
        val a = Interval(0, 5)
        val b = Interval(4, 7)

        val intervals = a.apply(b)

        assertEquals(1, intervals.size)
        assertEquals(Interval(0, 7), intervals[0])
    }

    @Test
    fun overlap_at_end() {
        val a = Interval(0, 5)
        val b = Interval(-5, 3)

        val intervals = a.apply(b)

        assertEquals(1, intervals.size)
        assertEquals(Interval(-5, 5), intervals[0])
    }

    @Test
    fun longer() {
        val a = Interval(0, 15)
        val b = Interval(3, 12)

        val intervals = a.apply(b)

        assertEquals(1, intervals.size)
        assertEquals(Interval(0, 15), intervals[0])
    }

    @Test
    fun shorter() {
        val a = Interval(0, 5)
        val b = Interval(-8, 12)

        val intervals = a.apply(b)

        assertEquals(1, intervals.size)
        assertEquals(Interval(-8, 12), intervals[0])
    }

}