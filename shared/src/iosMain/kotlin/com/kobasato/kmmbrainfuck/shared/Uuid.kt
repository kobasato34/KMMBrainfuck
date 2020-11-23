package com.kobasato.kmmbrainfuck.shared

import platform.Foundation.NSUUID

actual fun generateUUID(): String = NSUUID().UUIDString()
