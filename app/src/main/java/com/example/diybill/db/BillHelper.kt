package com.example.diybill.db

import com.example.diybill.config.OutlayType
import com.example.diybill.config.Config
import com.example.diybill.config.Type
import com.example.diybill.config.toIncomeType
import com.example.diybill.config.toOutlayType
import com.example.diybill.utils.Date
import com.example.diybill.utils.DateRange
import com.example.diybill.utils.toDate
import com.example.diybill.utils.toInt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

data class Bill(
    val id:Int,
    val name: String,
    val msg: String,
    val type: Type,
    val amount: String,
    val date: Date,
    val source:String,
    val urls:List<String>
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
                        bill.type is OutlayType,
                        bill.urls
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
                getAppDatabase().billDao().updateBill(
                    BaseBill(
                        bill.name,
                        bill.msg,
                        bill.type.cnName,
                        bill.amount,
                        bill.date.toInt,
                        Config.bookId,
                        bill.source,
                        bill.type is OutlayType,
                        bill.urls
                    ).apply {
                        id = bill.id
                    }
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
                        bill.type is OutlayType,
                        bill.urls
                    ).apply {
                        id = bill.id
                    }
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
                                bBill.id,
                                bBill.name,
                                bBill.msg,
                                if(bBill.isOutlay) bBill.type.toOutlayType!! else bBill.type.toIncomeType!!,
                                bBill.amount,
                                bBill.date.toDate,
                                bBill.source,
                                bBill.pictureUrls
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

    suspend fun collectBillListByDateRange(dateRange: DateRange,ioCallback: IOCallback<MFlow<List<Bill>>>){
        withContext(Dispatchers.IO){
            try {
                val flow = getAppDatabase().billDao().collectBillByDate(
                    Config.bookId,
                    dateRange.start.toInt,
                    dateRange.end.toInt
                )
                val mFlow =  MFlow(
                    flow.map {
                        it.map { bBill->
                            Bill(
                                bBill.id,
                                bBill.name,
                                bBill.msg,
                                if(bBill.isOutlay) bBill.type.toOutlayType!! else bBill.type.toIncomeType!!,
                                bBill.amount,
                                bBill.date.toDate,
                                bBill.source,
                                bBill.pictureUrls
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

    suspend fun queryBill(id: Int,ioCallback: IOCallback<Bill>){
        withContext(Dispatchers.IO){
            try {
                val r = getAppDatabase().billDao().queryBill(id)
                ioCallback.onCompleted(
                    Bill(
                        r.id,
                        r.name,
                        r.msg,
                        if(r.isOutlay) r.type.toOutlayType else r.type.toIncomeType!!,
                        r.amount,
                        r.date.toDate,
                        r.source,
                        r.pictureUrls
                    )
                )
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
                        it.id,
                        it.name,
                        it.msg,
                        it.type.toOutlayType,
                        it.amount,
                        it.date.toDate,
                        it.source,
                        it.pictureUrls
                    )
                }
                ioCallback.onCompleted(r)
            }catch (t:Throwable){
                ioCallback.onErrorHandler(t)
            }
        }
    }
}