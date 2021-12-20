package d18

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import readLines

internal class SnailFishNumberTest {
    @Test
    fun of() {
        val input = "[1,2]"
        val n = SnailFishNumber.of(input, null)

        assertEquals(input, n.toString())
        assertEquals(n.left?.parent, n)
        assertEquals(n.right?.parent, n)
    }

    @Test
    fun of2() {
        val input = "[[1,2],3]"
        val n = SnailFishNumber.of(input, null)
        assertEquals(input, n.toString())
    }

    @Test
    fun of3() {
        val input = "[[[[1,3],[5,3]],[[1,3],[8,7]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]"
        val n = SnailFishNumber.of(input, null)
        assertEquals(input, n.toString())
    }

    @Test
    fun add() {
        val a = SnailFishNumber.of("[1,2]", null)
        val b = SnailFishNumber.of("[[3,4],5]", null)

        val sum = a.add(b)

        assertEquals("[[1,2],[[3,4],5]]", sum.toString())
        assertEquals(sum, a.parent)
        assertEquals(sum, b.parent)
    }

    @Test
    fun reduce() {
        val n = SnailFishNumber.of("[[[[[9,8],1],2],3],4]", null)
        val values = getValues(n)
        n.reduce(values, "explode")
        n.reduce(values, "split")

        assertEquals( "[[[[0,9],2],3],4]", n.toString())
    }

    @Test
    fun reduce2() {
        val n = SnailFishNumber.of("[7,[6,[5,[4,[3,2]]]]]", null)
        val values = getValues(n)
        n.reduce(values, "explode")
        n.reduce(values, "split")

        assertEquals( "[7,[6,[5,[7,0]]]]", n.toString())
    }

    @Test
    fun reduce3() {
        val n = SnailFishNumber.of("[[6,[5,[4,[3,2]]]],1]", null)
        val values = getValues(n)
        n.reduce(values, "explode")
        n.reduce(values, "split")

        assertEquals("[[6,[5,[7,0]]],3]", n.toString())
    }

    @Test
    fun reduce4() {
        val n = SnailFishNumber.of("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]", null)

        var reduce = true
        while (reduce) {
            reduce = n.reduce(getValues(n), "explode") || n.reduce(getValues(n), "split")
        }

        assertEquals( "[[3,[2,[8,0]]],[9,[5,[7,0]]]]", n.toString())
    }

    @Test
    fun split() {
        val n = SnailFishNumber.of("[11,2]", null)
        var reduce = true
        while (reduce) {
            reduce = n.reduce(getValues(n), "explode") || n.reduce(getValues(n), "split")
        }

        assertEquals("[[5,6],2]", n.toString())
    }

    @Test
    fun compute() {
        val a = SnailFishNumber.of("[[[[4,3],4],4],[7,[[8,4],9]]]", null)
        val b = SnailFishNumber.of("[1,1]", null)
        val r = a.add(b)
        var reduce = true
        while (reduce) {
            reduce = r.reduce(getValues(r), "explode") || r.reduce(getValues(r), "split")
        }

        assertEquals("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]", r.toString())
    }

    @Test
    fun addAll_1() {
        val lines = mutableListOf(
            "[1,1]",
            "[2,2]",
            "[3,3]",
            "[4,4]")
        assertEquals("[[[[1,1],[2,2]],[3,3]],[4,4]]", addAll(lines).toString())
    }

    @Test
    fun addAll_2() {
        val lines = mutableListOf(
            "[1,1]",
            "[2,2]",
            "[3,3]",
            "[4,4]",
            "[5,5]",
            "[6,6]")
        assertEquals("[[[[5,0],[7,4]],[5,5]],[6,6]]", addAll(lines).toString())
    }

    @Test
    fun part1_3() {
        val a = SnailFishNumber.of("[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]", null)
        val b = SnailFishNumber.of("[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]", null)
        val r = a.add(b)

        var reduce = true
        while (reduce) {
            reduce = r.reduce(getValues(r), "explode") || r.reduce(getValues(r), "split")
        }

        assertEquals("[[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[7,9],[5,0]]]]", r.toString())
    }

    @Test
    fun magnitude() {
        val n = SnailFishNumber.of("[9,1]", null)

        assertEquals(29, n.magnitude())
    }

    @Test
    fun magnitude_2() {
        val n = SnailFishNumber.of("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]", null)

        assertEquals(3488, n.magnitude())
    }

    @Test
    fun part1_4() {
        assertEquals(4140, part1(readLines("src/main/resources/d18/test2.txt", String::class)))
    }

    @Test
    fun part2_1() {
        assertEquals(3993, part2(readLines("src/main/resources/d18/test2.txt", String::class)))
    }

    @Test
    fun reduce5() {
        var n = SnailFishNumber.of("[[[[[7,7],[7,8]],[[9,5],[8,7]]],[[[6,8],[0,8]],[[9,9],[9,0]]]],[[2,[2,2]],[8,[8,1]]]]", null)

        var reduce = true
        while (reduce) {
            reduce = n.reduce(getValues(n), "explode") || n.reduce(getValues(n), "split")
        }

        assertEquals("[[[[6,6],[6,6]],[[6,0],[6,7]]],[[[7,7],[8,9]],[8,[8,1]]]]", n.toString())
    }

    @Test
    fun reduce6() {
        var n = SnailFishNumber.of("[[[[[6,7],[6,7]],[[7,7],[0,7]]],[[[8,7],[7,7]],[[8,8],[8,0]]]],[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]]", null)

        var reduce = true
        while (reduce) {
            reduce = n.reduce(getValues(n), "explode") || n.reduce(getValues(n), "split")
        }

        assertEquals("[[[[7,0],[7,7]],[[7,7],[7,8]]],[[[7,7],[8,8]],[[7,7],[8,7]]]]", n.toString())
    }
}