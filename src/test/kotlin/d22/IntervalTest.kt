package d22

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class IntervalTest {

    @Test
    fun add_no_overlap() {
        val a = Interval(0, 5, true)
        val b = Interval(10, 12, true)

        val intervals = a.apply(b)

        assertEquals(2, intervals.size)
        assertEquals(Interval(0, 5, true), intervals[0])
        assertEquals(Interval(10, 12, true), intervals[1])
    }

    @Test
    fun add_overlap_at_start() {
        val a = Interval(0, 5, true)
        val b = Interval(4, 7, true)

        val intervals = a.apply(b)

        assertEquals(1, intervals.size)
        assertEquals(Interval(0, 7, true), intervals[0])
    }

    @Test
    fun add_overlap_at_end() {
        val a = Interval(0, 5, true)
        val b = Interval(-5, 3, true)

        val intervals = a.apply(b)

        assertEquals(1, intervals.size)
        assertEquals(Interval(-5, 5, true), intervals[0])
    }

    @Test
    fun add_longer() {
        val a = Interval(0, 15, true)
        val b = Interval(3, 12, true)

        val intervals = a.apply(b)

        assertEquals(1, intervals.size)
        assertEquals(Interval(0, 15, true), intervals[0])
    }

    @Test
    fun add_shorter() {
        val a = Interval(0, 5, true)
        val b = Interval(-8, 12, true)

        val intervals = a.apply(b)

        assertEquals(1, intervals.size)
        assertEquals(Interval(-8, 12, true), intervals[0])
    }

    @Test
    fun remove_no_overlap() {
        val a = Interval(0, 5, true)
        val b = Interval(10, 12, false)

        val intervals = a.apply(b)

        assertEquals(1, intervals.size)
        assertEquals(Interval(0, 5, true), intervals[0])
    }

    @Test
    fun remove_overlap_at_start() {
        val a = Interval(0, 5, true)
        val b = Interval(4, 7, false)

        val intervals = a.apply(b)

        assertEquals(1, intervals.size)
        assertEquals(Interval(0, 4, true), intervals[0])
    }

    @Test
    fun remove_overlap_at_end() {
        val a = Interval(0, 5, true)
        val b = Interval(-5, 3, false)

        val intervals = a.apply(b)

        assertEquals(1, intervals.size)
        assertEquals(Interval(3, 5, true), intervals[0])
    }

    @Test
    fun remove_longer() {
        val a = Interval(0, 15, true)
        val b = Interval(3, 12, false)

        val intervals = a.apply(b)

        assertEquals(2, intervals.size)
        assertEquals(Interval(0, 3, true), intervals[0])
        assertEquals(Interval(12, 15, true), intervals[1])
    }

    @Test
    fun remove_shorter() {
        val a = Interval(0, 5, true)
        val b = Interval(-8, 12, false)

        val intervals = a.apply(b)

        assertEquals(0, intervals.size)
    }

    @Test
    fun reduce() {
        val axis = Axis(mutableListOf(Interval(0, 5, true)))

        axis.reduce(Interval(3, 7, true))

        assertEquals(1, axis.intervals.size)
        assertEquals(Interval(0, 7, true), axis.intervals[0])
    }

    @Test
    fun reduce2() {
        val axis = Axis(mutableListOf(Interval(0, 5, true)))

        axis.reduce(Interval(3, 8, true))
        axis.reduce(Interval(4, 6, false))

        assertEquals(2, axis.intervals.size)
        assertEquals(Interval(0, 3, true), axis.intervals[0])
        assertEquals(Interval(7, 8, true), axis.intervals[1])
    }

    @Test
    fun reduce3() {
        val axis = Axis(mutableListOf(Interval(-50, -15, true), Interval(5, 44, true)))
        axis.reduce(Interval(-7, 46, true))

        assertEquals(2, axis.intervals.size)
        assertEquals(Interval(-50, -15, true), axis.intervals[0])
        assertEquals(Interval(-7, 46, true), axis.intervals[1])
    }

    @Test
    fun no_overlap() {
        val a = Interval(0, 5, true)
        val b = Interval(6, 10, true)

        assertEquals(false, a.overlap(b))
    }

    @Test
    fun overlap_all() {
        val a = Interval(0, 10, true)
        val b = Interval(6, 8, true)

        assertEquals(true, a.overlap(b))
    }

    @Test
    fun overlap_left() {
        val a = Interval(5, 10, true)
        val b = Interval(0, 7, true)

        assertEquals(true, a.overlap(b))
    }

    @Test
    fun overlap_right() {
        val a = Interval(0, 5, true)
        val b = Interval(3, 10, true)

        assertEquals(true, a.overlap(b))
    }

    @Test
    fun overlap() {
        val a = Interval(5, 44, true)
        val b = Interval(-7, 46, true)

        assertEquals(true, a.overlap(b))
    }

}