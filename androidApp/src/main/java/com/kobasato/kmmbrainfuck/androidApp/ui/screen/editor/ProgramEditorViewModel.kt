package com.kobasato.kmmbrainfuck.androidApp.ui.screen.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kobasato.kmmbrainfuck.db.Program
import com.kobasato.kmmbrainfuck.shared.ProgramService
import com.kobasato.kmmbrainfuck.shared.brainfuck.Interpreter
import com.kobasato.kmmbrainfuck.shared.brainfuck.Output
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProgramEditorViewModel(
    private val programService: ProgramService,
    initialTitle: String,
    initialInput: String
) : ViewModel() {

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state

    init {
        _state.value = _state.value.copy(title = initialTitle, input = initialInput)
    }

    constructor(programService: ProgramService, program: Program) : this(
        programService,
        program.title,
        program.input
    ) {
        _state.value = _state.value.copy(program = program)
    }

    fun onTitleChange(value: String) {
        _state.value = _state.value.copy(title = value)
    }

    fun onInputChange(value: String) {
        _state.value = _state.value.copy(input = value)
    }

    fun runProgram() {
        val result = Interpreter.execute(_state.value.input)
        _state.value = _state.value.copy(output = result)
    }

    fun saveProgram() {
        viewModelScope.launch {
            val program = _state.value.program
            if (program == null) {
                programService.saveProgram(_state.value.title, _state.value.input)
            } else {
                programService.updateProgram(program, _state.value.input)
            }
        }
    }

    data class State(
        val program: Program? = null,
        val title: String = "",
        val input: String = "",
        val output: Output = Output.Success(""),
    )
}
