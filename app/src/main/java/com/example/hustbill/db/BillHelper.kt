package com.example.hustbill.db

import com.example.hustbill.config.BillType
import com.example.hustbill.utils.Date
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class Bill(
    val name: String,
    val msg: String,
    val type: BillType,
    val amount: Int,
    val date: Date
)

object BillHelper {

    suspend fun insertBill(
        bill: Bill,
        ioCallback: IOCallback<Unit>,
    ) {
        withContext(Dispatchers.IO) {
            try {
                ioCallback.onCompleted(Unit)
            } catch (t: Throwable) {
                ioCallback.onErrorHandler(t)
            }
        }
    }
}