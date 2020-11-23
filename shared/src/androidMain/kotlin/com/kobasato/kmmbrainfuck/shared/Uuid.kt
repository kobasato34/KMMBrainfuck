package com.kobasato.kmmbrainfuck.shared

import java.util.UUID

actual fun generateUUID(): String = UUID.randomUUID().toString()
