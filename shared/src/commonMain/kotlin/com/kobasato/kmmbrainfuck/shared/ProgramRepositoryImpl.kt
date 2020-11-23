package com.kobasato.kmmbrainfuck.shared

import com.kobasato.kmmbrainfuck.db.Program
import com.kobasato.kmmbrainfuck.db.ProgramQueries
import com.kobasato.kmmbrainfuck.shared.db.AppDatabase
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ProgramRepositoryImpl(database: AppDatabase) : ProgramRepository {
    private val queries: ProgramQueries = database.programQueries

    override suspend fun addOrUpdate(program: Program) = withContext(Dispatchers.Default) {
        queries.insertOrUpdate(id = program.id, title = program.title, input = program.input)
    }

    override fun getAll(): Flow<List<Program>> {
        return queries
            .selectAll()
            .asFlow()
            .mapToList(Dispatchers.Default)
    }
}
