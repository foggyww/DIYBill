package com.example.diybill.config

import com.example.diybill.utils.DateLevel
import com.example.diybill.utils.DateRange
import com.example.diybill.utils.SP
import com.example.diybill.utils.getDate
import com.example.diybill.utils.getRange
import com.example.diybill.utils.toDateRange
import com.example.diybill.utils.toString

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


    var openOver:Boolean = SP.getBoolean(SIGNAL.OPEN_OVER,false)
        set(value) {
            SP.setBoolean(SIGNAL.OPEN_OVER,value)
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
        const val OPEN_AUTO = "start_auto"
        const val OPEN_OVER = "open_over"
    }
}