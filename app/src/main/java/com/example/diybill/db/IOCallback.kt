package com.example.diybill.db

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class IOCallback<T>(
    private val onSuccess: ((T) -> Unit)? = null,
    private val onError: ((Throwable) -> Unit)? = null,
    private val onCompleted: (suspend (T) -> Unit)? = null,
    private val onErrorHandler: (suspend (Throwable) -> Unit)? = null,
) {
    suspend fun onCompleted(value: T) {
        withContext(Dispatchers.Main) {
            onSuccess?.invoke(value)
        }

        withContext(Dispatchers.IO) {
            onCompleted?.invoke(value)
        }
    }

    suspend fun onErrorHandler(throwable: Throwable) {
        withContext(Dispatchers.Main) {
            onError?.invoke(throwable)
        }

        withContext(Dispatchers.IO) {
            onErrorHandler?.invoke(throwable)
        }
    }
}