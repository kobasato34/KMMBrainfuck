package com.kobasato.kmmbrainfuck.shared

import com.kobasato.kmmbrainfuck.shared.db.AppDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(AppDatabase.Schema, dbName)
    }
}
