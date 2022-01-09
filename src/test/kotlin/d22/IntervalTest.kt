package d22

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class IntervalTest {

    @Test
    fun intersect_start() {
        val a = Interval(12, 20)
        val b = Interval(1, 15)

        assertTrue(a.intersect(b))
        assertTrue(b.intersect(a))
    }

    @Test
    fun intersect_end() {
        val a = Interval(0, 5)
        val b = Interval(1, 15)

        assertTrue(a.intersect(b))
        assertTrue(b.intersect(a))
    }

    @Test
    fun intersect_full() {
        val a = Interval(5, 10)
        val b = Interval(0, 15)

        assertTrue(a.intersect(b))
        assertTrue(b.intersect(a))
    }

    @Test
    fun no_intersect() {
        val a = Interval(0, 5)
        val b = Interval(10, 15)

        assertFalse(a.intersect(b))
        assertFalse(b.intersect(a))
    }

    @Test
    fun cube_intersect_same() {
        val a = Cube2(Interval(0, 3), Interval(0, 3), Interval(0, 3), true)
        val b = Cube2(Interval(0, 3), Interval(0, 3), Interval(0, 3), true)

        assertTrue(a.intersect(b))
        assertTrue(b.intersect(a))
    }

    @Test
    fun cube_intersect() {
        val a = Cube2(Interval(0, 3), Interval(0, 3), Interval(0, 3), true)
        val b = Cube2(Interval(3, 6), Interval(3, 6), Interval(3, 6), true)

        assertTrue(a.intersect(b))
        assertTrue(b.intersect(a))
    }

    @Test
    fun cube_off() {
        val a = Cube2(Interval(0, 2), Interval(0, 2), Interval(0, 2), true)
        val b = Cube2(Interval(2, 4), Interval(2, 4), Interval(2, 4), false)

        val expected = mutableSetOf<Cube2>()
        expected.add(Cube2(Interval(0, 2), Interval(0, 2), Interval(0, 1), true))
        expected.add(Cube2(Interval(2, 2), Interval(0, 2), Interval(0, 1), true))
        expected.add(Cube2(Interval(2, 2), Interval(0, 2), Interval(0, 1), true))
        assertEquals(expected, a.delta(b))
    }

}