package com.kobasato.kmmbrainfuck.shared

import com.kobasato.kmmbrainfuck.shared.db.AppDatabase
import com.squareup.sqldelight.db.SqlDriver
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.provider
import org.kodein.di.singleton
import org.kodein.di.instance

val sharedModule = DI.Module("shared") {
    bind<SqlDriver>() with provider { instance<DatabaseDriverFactory>().createDriver() }
    bind<AppDatabase>() with singleton { AppDatabase(instance()) }
    bind<ProgramRepository>() with provider { ProgramRepositoryImpl(instance()) }
    bind<ProgramService>() with provider { ProgramService(instance()) }
}
