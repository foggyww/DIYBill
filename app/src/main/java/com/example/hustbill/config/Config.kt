package com.example.hustbill.config

import com.example.hustbill.utils.DateLevel
import com.example.hustbill.utils.DateRange
import com.example.hustbill.utils.SP
import com.example.hustbill.utils.getDate
import com.example.hustbill.utils.getRange
import com.example.hustbill.utils.toDateRange
import com.example.hustbill.utils.toString

object Config {

    const val NAME_MAX_LENGTH = 10

    const val AMOUNT_MAX_LENGTH = 5

    var budget:Int = SP.getInt(SIGNAL.BUDGET,2000)
        set(value){
            SP.setInt(SIGNAL.BUDGET,value)
            field = value
        }

    var bookId:Int = SP.getInt(SIGNAL.BOOK_ID,0)
        set(value) {
            SP.setInt(SIGNAL.BOOK_ID,value)
            field = value
        }

    var monthRange:DateRange = SP.get(SIGNAL.MONTH_RANGE)?.toDateRange
        ?:getDate().getRange(DateLevel.MONTH)
        set(value) {
            SP.set(SIGNAL.MONTH_RANGE,value.toString)
            field = value
        }

    private object SIGNAL{
        const val BUDGET = "budget"
        const val BOOK_ID = "book_id"
        const val MONTH_RANGE = "month_range"
    }
}