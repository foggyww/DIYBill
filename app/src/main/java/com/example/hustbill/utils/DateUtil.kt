package com.example.hustbill.utils

import java.util.Calendar

data class Date(
    val year:Int,
    val month:Int,
    val day:Int
)

fun getDate() : Date{
    val cal = Calendar.getInstance()
    val year = cal.get(Calendar.YEAR)
    val month = cal.get(Calendar.MONTH)+1
    val day = cal.get(Calendar.DAY_OF_MONTH)
    return Date(
        year,
        month,
        day
    )
}

data class Month(
    val year: Int,
    val month: Int
)

enum class DateLevel(val index:Int,val chineseName:String){
    DAY(0,"日"),
    WEEK(1,"周"),
    MONTH(2,"月"),
    YEAR(3,"年"),
}

val Date.toString:String
    get() {
        val yearStr = year.toString()
        val monthStr = if(month<10) "0$month"
        else month.toString()
        val dayStr = if(day<10) "0$day"
        else day.toString()
        return "${yearStr}年${monthStr}月${dayStr}日"
    }

val Date.toInt:Int
    get() =
        this.year*10000+this.month*100+this.day


data class DateRange(
    val start:Date,
    val end:Date
)

operator fun Date.plus(other:Int):Date{
    val calendar = Calendar.getInstance()
    calendar.set(year, month - 1, day) // 月份从 0 开始，需要减去 1
    calendar.add(Calendar.DAY_OF_YEAR,other)
    return Date(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH))
}

fun Date.getRange(dateLevel: DateLevel):DateRange{
    return when(dateLevel){
        DateLevel.DAY->{
            DateRange(this,this)
        }
        DateLevel.WEEK->{
            val calendar = Calendar.getInstance()
            calendar.set(year, month - 1, day) // 月份从 0 开始，需要减去 1
            calendar.firstDayOfWeek = Calendar.MONDAY
            calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY)
            val front = Date(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH)+1,
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            calendar.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY)
            val back = Date(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH)+1,
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            DateRange(front, back)
        }
        DateLevel.MONTH->{
            val calendar = Calendar.getInstance()
            calendar.set(year, month - 1, day)
            calendar.set(Calendar.DAY_OF_MONTH,1)
            val front = Date(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH)+1,
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
            val back = Date(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH)+1,
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            DateRange(front, back)
        }
        DateLevel.YEAR->{
            val calendar = Calendar.getInstance()
            calendar.set(year, month - 1, day)
            calendar.set(Calendar.DAY_OF_YEAR,1)
            val front = Date(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH)+1,
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            calendar.set(Calendar.DAY_OF_YEAR,calendar.getActualMaximum(Calendar.DAY_OF_YEAR))
            val back = Date(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH)+1,
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            DateRange(front, back)
        }
    }
}

operator fun Date.compareTo(other: Date):Int{
    val thisValue = this.year*10000+this.month*100+this.day
    val otherValue = other.year*10000+other.month*100+other.day
    return if(thisValue>otherValue){
        1
    }else if(thisValue==otherValue){
        0
    }else{
        -1
    }
}