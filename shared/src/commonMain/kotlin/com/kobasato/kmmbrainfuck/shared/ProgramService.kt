package com.kobasato.kmmbrainfuck.shared

import com.kobasato.kmmbrainfuck.db.Program
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

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

    fun getPrograms(): Flow<List<Program>> {
        return programRepository.getAll()
    }

    // for iOS
    fun getPrograms(block: (List<Program>) -> Unit): Closeable {
        val job = Job()

        getPrograms()
            .onEach {
                block(it)
            }
            .launchIn(CoroutineScope(Dispatchers.Main + job))

        return object : Closeable {
            override fun close() {
                job.cancel()
            }
        }
    }
}
