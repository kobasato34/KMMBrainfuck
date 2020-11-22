package com.kobasato.kmmbrainfuck.shared

import com.squareup.sqldelight.db.SqlDriver

internal const val dbName = "app.db"

expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}
