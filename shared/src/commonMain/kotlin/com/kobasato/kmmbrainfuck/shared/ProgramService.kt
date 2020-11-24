package com.kobasato.kmmbrainfuck.shared

import com.kobasato.kmmbrainfuck.db.Program

class ProgramService(private val programRepository: ProgramRepository) {
    suspend fun saveProgram(title: String, input: String): Program {
        val program = Program(id = generateUUID(), title = title, input = input)
        programRepository.addOrUpdate(program)
        return program
    }

    suspend fun updateProgram(program: Program, newInput: String): Program {
        val updatedProgram = program.copy(input = newInput)
        programRepository.addOrUpdate(updatedProgram)
        return program
    }

    fun getPrograms(): CommonFlow<List<Program>> {
        return programRepository
            .getAll()
            .wrap()
    }
}
