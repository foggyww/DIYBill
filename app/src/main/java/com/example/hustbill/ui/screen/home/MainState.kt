package com.example.hustbill.ui.screen.home

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.example.hustbill.config.Config
import com.example.hustbill.db.Bill
import com.example.hustbill.utils.Date
import com.example.hustbill.utils.getDate
import com.example.hustbill.utils.plus
import com.example.hustbill.utils.toInt

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