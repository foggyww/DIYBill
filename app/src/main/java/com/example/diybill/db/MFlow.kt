package com.example.diybill.db

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext

class MFlow<V>(private val flow: Flow<V>) {

    private var complete: (suspend (V) -> Unit)? = null
    private var success: ((V) -> Unit)? = null
    private var error: ((Throwable) -> Unit)? = null

    fun onSuccess(action: (V) -> Unit): MFlow<V> {
        success = action
        return this
    }

    fun onComplete(action: suspend (V) -> Unit): MFlow<V> {
        complete = action
        return this
    }

    fun onError(action: (Throwable) -> Unit): MFlow<V> {
        error = action
        return this
    }

    fun <T, R> combine(mFlow: MFlow<T>, transform: suspend (a: V, b: T) -> R): MFlow<R> {
        return MFlow(flow.combine(mFlow.flow, transform))
    }


    suspend fun execute() {
        withContext(Dispatchers.IO) {
            flow
                .catch {
                    withContext(Dispatchers.Main) {
                        this@MFlow.error?.invoke(it)
                    }
                }
                .collect { v ->
                    withContext(Dispatchers.Main) {
                        this@MFlow.success?.invoke(v)
                    }
                    this@MFlow.complete?.invoke(v)
                }
        }
    }
}