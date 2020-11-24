package com.kobasato.kmmbrainfuck.shared

import org.kodein.di.DI
import org.kodein.di.provider
import org.kodein.di.direct
import org.kodein.di.instance
import org.kodein.di.bind

class Injector {
    private val container = DI.lazy {
        import(sharedModule)
        import(iosModule)
    }

    private val iosModule = DI.Module("iosModule") {
        bind<DatabaseDriverFactory>() with provider { DatabaseDriverFactory() }
    }

    fun programService(): ProgramService = container.direct.instance()
}
