package com.kobasato.kmmbrainfuck.androidApp

import android.app.Application
import com.kobasato.kmmbrainfuck.shared.DatabaseDriverFactory
import com.kobasato.kmmbrainfuck.shared.sharedModule
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.bind
import org.kodein.di.provider

class MyApplication : Application(), DIAware {
    override val di by DI.lazy {
        import(androidXModule(this@MyApplication))
        import(sharedModule)
        bind<DatabaseDriverFactory>() with provider { DatabaseDriverFactory(this@MyApplication) }
    }
}
