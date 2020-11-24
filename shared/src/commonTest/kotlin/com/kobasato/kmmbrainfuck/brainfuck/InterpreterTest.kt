package com.kobasato.kmmbrainfuck.brainfuck

import com.kobasato.kmmbrainfuck.shared.brainfuck.Interpreter
import com.kobasato.kmmbrainfuck.shared.brainfuck.Output
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InterpreterTest {
    @Test
    fun helloWorld() {
        val input =
            ">+++++++++[<++++++++>-]<.>+++++++[<++++>-]<+.+++++++..+++.[-]>++++++++[<++++>-]<.>+++++++++++[<+++++>-]<.>++++++++[<+++>-]<.+++.------.--------.[-]>++++++++[<++++>-]<+."
        val output = Interpreter.execute(input)

        val expected = Output.Success("Hello World!")
        assertEquals(expected, output)
    }

    @Test
    fun plus() {
        val input =
            "+++>+++><<[->[->>+<<]>>[-<+<+>>]<<<]>>++++++++++++++++++++++++++++++++++++++++++++++++."
        val output = Interpreter.execute(input)

        val expected = Output.Success("9")
        assertEquals(expected, output)
    }

    @Test
    fun outOfRange() {
        val input = "<"
        val output = Interpreter.execute(input)

        assertTrue { output is Output.Error }
    }
}
