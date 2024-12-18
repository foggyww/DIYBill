package com.example.diybill.ui.screen.home

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.example.diybill.config.Config
import com.example.diybill.db.Bill
import com.example.diybill.utils.Date
import com.example.diybill.utils.getDate
import com.example.diybill.utils.plus
import com.example.diybill.utils.toDate
import com.example.diybill.utils.toInt

@Immutable
@Stable
data class MainState(
    val billState: BillState = BillState()
)

@Immutable
@Stable
data class BillState(
    val dayBillList:List<DayBill> = emptyList()
){
    val total :Int get() =
        dayBillList.sumOf {
            it.billList.sumOf { b->
                b.amount.toFloat().toInt()
            }
        }
}

fun BillState.getWeekBillList():List<DayBill>{
    val nowDate = getDate()
    val weekBillList = mutableListOf<DayBill>()
    for (i in 0..6){
        val date = nowDate + (-i)
        var flag = true
        for(dayBill in this.dayBillList){
            if(date==dayBill.date){
                weekBillList.add(dayBill)
                flag = false
                break
            }
        }
        if(flag){
            weekBillList.add(DayBill(date, emptyList()))
        }
    }
    weekBillList.sortBy {
        it.date.toInt
    }
    return weekBillList.toList()
}

class MainConfig{
    val budge by Config::budget
}

data class DayBill(
    val date: Date,
    val billList: List<Bill>
)

val List<Bill>.toDayBillList : List<DayBill>
    get() {
        val map = mutableMapOf<Int,MutableList<Bill>>()
        this.forEach {
            map.getOrPut(it.date.toInt) {
                mutableListOf()
            }.add(it)
        }
        return map.map {
            DayBill(it.key.toDate,it.value.reversed())
        }.sortedBy {
            -it.date.toInt
        }
    }