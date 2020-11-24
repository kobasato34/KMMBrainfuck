package com.kobasato.kmmbrainfuck.shared

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

interface Closeable {
    fun close()
}

fun <T> Flow<T>.wrap(dispatcher: CoroutineDispatcher = Dispatchers.Main): CommonFlow<T> =
    CommonFlow(this, dispatcher)

class CommonFlow<T>(private val origin: Flow<T>, private val dispatcher: CoroutineDispatcher) :
    Flow<T> by origin {
    fun watch(block: (T) -> Unit): Closeable {
        val job = Job()

        onEach {
            block(it)
        }.launchIn(CoroutineScope(dispatcher + job))

        return object : Closeable {
            override fun close() {
                job.cancel()
            }
        }
    }
}
