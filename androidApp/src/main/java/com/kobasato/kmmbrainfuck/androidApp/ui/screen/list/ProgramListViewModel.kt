package com.kobasato.kmmbrainfuck.androidApp.ui.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kobasato.kmmbrainfuck.db.Program
import com.kobasato.kmmbrainfuck.shared.ProgramService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProgramListViewModel(private val programService: ProgramService) : ViewModel() {

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state

    init {
        viewModelScope.launch {
            programService
                .getPrograms()
                .collect {
                    _state.value = _state.value.copy(programList = it)
                }
        }
    }

    data class State(
        val programList: List<Program> = emptyList(),
    )
}
