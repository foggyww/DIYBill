package com.example.hustbill.db

import com.example.hustbill.config.OutlayType
import com.example.hustbill.config.Config
import com.example.hustbill.config.Type
import com.example.hustbill.config.toIncomeType
import com.example.hustbill.config.toOutlayType
import com.example.hustbill.utils.Date
import com.example.hustbill.utils.toDate
import com.example.hustbill.utils.toInt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

data class Bill(
    val name: String,
    val msg: String,
    val type: Type,
    val amount: String,
    val date: Date,
    val source:String,
)

const val FROM_OHTER = 0
const val FROM_WEIXIN = 1
const val FROM_ALYPAY= 2

object BillHelper {

    suspend fun insertBill(
        bill: Bill,
        ioCallback: IOCallback<Unit>,
    ) {
        withContext(Dispatchers.IO) {
            try {
                getAppDatabase().billDao().insertBill(
                    BaseBill(
                        bill.name,
                        bill.msg,
                        bill.type.cnName,
                        bill.amount,
                        bill.date.toInt,
                        Config.bookId,
                        bill.source,
                        bill.type is OutlayType
                    )
                )
                ioCallback.onCompleted(Unit)
            } catch (t: Throwable) {
                ioCallback.onErrorHandler(t)
            }
        }
    }

    suspend fun updateBill(
        bill: Bill,
        ioCallback: IOCallback<Unit>,
    ){
        withContext(Dispatchers.IO) {
            try {
                getAppDatabase().billDao().insertBill(
                    BaseBill(
                        bill.name,
                        bill.msg,
                        bill.type.cnName,
                        bill.amount,
                        bill.date.toInt,
                        Config.bookId,
                        bill.source,
                        bill.type is OutlayType
                    )
                )
                ioCallback.onCompleted(Unit)
            } catch (t: Throwable) {
                ioCallback.onErrorHandler(t)
            }
        }
    }

    suspend fun deleteBill(
        bill: Bill,
        ioCallback: IOCallback<Unit>,
    ){
        withContext(Dispatchers.IO) {
            try {
                getAppDatabase().billDao().deleteBill(
                    BaseBill(
                        bill.name,
                        bill.msg,
                        bill.type.cnName,
                        bill.amount,
                        bill.date.toInt,
                        Config.bookId,
                        bill.source,
                        bill.type is OutlayType
                    )
                )
                ioCallback.onCompleted(Unit)
            } catch (t: Throwable) {
                ioCallback.onErrorHandler(t)
            }
        }
    }

    suspend fun collectOutlayList(ioCallback: IOCallback<MFlow<List<Bill>>>){
        withContext(Dispatchers.IO){
            try {
                val flow = getAppDatabase().billDao().collectOutlayByDate(
                    Config.bookId,
                    Config.monthRange.start.toInt,
                    Config.monthRange.end.toInt
                )
                val mFlow =  MFlow(
                    flow.map {
                        it.map { bBill->
                            Bill(
                                bBill.name,
                                bBill.msg,
                                if(bBill.isOutlay) bBill.type.toOutlayType!! else bBill.type.toIncomeType!!,
                                bBill.amount,
                                bBill.date.toDate,
                                bBill.source,
                            )
                        }
                    }
                )
                ioCallback.onCompleted(mFlow)
            }catch (t:Throwable){
                ioCallback.onErrorHandler(t)
            }
        }
    }

    suspend fun queryBillList(ioCallback: IOCallback<List<Bill>>){
        withContext(Dispatchers.IO){
            try {
                val r = getAppDatabase().billDao().queryBillByDate(
                    Config.bookId,
                    Config.monthRange.start.toInt,
                    Config.monthRange.end.toInt
                ).map {
                    Bill(
                        it.name,
                        it.msg,
                        it.type.toOutlayType!!,
                        it.amount,
                        it.date.toDate,
                        it.source,
                    )
                }
                ioCallback.onCompleted(r)
            }catch (t:Throwable){
                ioCallback.onErrorHandler(t)
            }
        }
    }
}