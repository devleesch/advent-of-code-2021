package d24

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class AluTest {

    @Test
    fun mod() {
        val alu = Alu(4)
        alu.exec("inp x")
        alu.exec("mod x 3")

        assertEquals(1, alu.vars['x'])
    }

    @Test
    fun mul() {
        val alu = Alu(26)
        alu.exec("inp z")
        alu.exec("inp x")
        alu.exec("mul z 3")
        alu.exec("eql z x")

        assertEquals(1, alu.vars['z'])
    }

    @Test
    fun add() {
        val alu = Alu(2)
        alu.exec("inp z")
        alu.exec("add z -13")

        assertEquals(-11, alu.vars['z'])
    }

}