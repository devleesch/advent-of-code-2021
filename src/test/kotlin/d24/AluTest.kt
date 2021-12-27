package d24

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class AluTest {

    @Test
    fun inp() {
        val alu = Alu(7)
        alu.exec("inp x")

        assertEquals(7, alu.vars['x'])
    }

    @Test
    fun add() {
        val alu = Alu(2)
        alu.exec("inp z")
        alu.exec("add z -13")
        alu.exec("add z 20")

        assertEquals(9, alu.vars['z'])
    }

    @Test
    fun mul() {
        val alu = Alu(2)
        alu.exec("inp z")
        alu.exec("mul z 2")
        alu.exec("mul z 2")

        assertEquals(8, alu.vars['z'])
    }

    @Test
    fun div() {
        val alu = Alu(5)
        alu.exec("inp x")
        alu.exec("div x 2")

        assertEquals(2, alu.vars['x'])
    }

    @Test
    fun mod() {
        val alu = Alu(4)
        alu.exec("inp x")
        alu.exec("mod x 3")

        assertEquals(1, alu.vars['x'])
    }

    @Test
    fun eql() {
        val alu = Alu(6)
        alu.exec("inp y")
        alu.exec("eql y 6")
        alu.exec("eql z 7")

        assertEquals(1, alu.vars['y'])
        assertEquals(0, alu.vars['z'])
    }

}