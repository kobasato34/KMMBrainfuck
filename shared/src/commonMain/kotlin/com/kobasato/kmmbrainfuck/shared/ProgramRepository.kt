package com.kobasato.kmmbrainfuck.shared

import com.kobasato.kmmbrainfuck.db.Program
import kotlinx.coroutines.flow.Flow

interface ProgramRepository {
    suspend fun addOrUpdate(program: Program)
    fun getAll(): Flow<List<Program>>
}
