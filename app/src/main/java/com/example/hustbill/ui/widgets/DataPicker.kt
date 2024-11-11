package com.example.hustbill.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.hustbill.ui.theme.CardShapes
import com.example.hustbill.ui.theme.colors
import com.example.hustbill.utils.getDate
import kotlin.math.min

/**
 * 日期选择器;
 */
@Composable
fun DatePicker(
    year: Int =getDate().year,
    month: Int = getDate().month,
    day: Int = getDate().day,
    onDateSelected: (Int,Int,Int) -> Unit,
) {
    val nowYear = remember { mutableIntStateOf(year) }
    val nowMonth = remember { mutableIntStateOf(month) }
    val nowDay = remember { mutableIntStateOf(day) }
    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
    ) {
        DateWheel(nowYear.intValue,nowMonth.intValue,nowDay.intValue) { index, value ->
            when(index){
                0 -> {
                    nowYear.intValue = value
                }
                1 -> {
                    nowMonth.intValue = value
                }
                2 -> {
                    nowDay.intValue = value
                }
            }
            onDateSelected(nowYear.intValue,nowMonth.intValue,nowDay.intValue)
        }
    }

}

/**
 * 时间选择器 - 睡眠 - (开始-结束时间)
 */
@Composable
private fun DateWheel(
    year: Int,
    month: Int,
    day: Int,
    onChange: (index: Int, value: Int) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        Alignment.Center
    ) {
        val measureHeight = remember { mutableIntStateOf(0) }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(38.dp)
                .align(Alignment.Center)
                .background(color = colors.background, shape = CardShapes.large)
                .border(1.dp, colors.border, CardShapes.large)
        ) {

        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .onGloballyPositioned {
                    measureHeight.intValue = it.size.height
                }
        ) {
            val modifier = Modifier.weight(1f)

            //  年
            ColumnPicker(
                modifier = modifier,
                value = year,
                label = { "${it}年" },
                range = 2024..2060,
                onValueChange = {
                    onChange(0, it)
                }
            )
            Box(modifier = Modifier
                .width(2.dp)
                .height(with(LocalDensity.current){
                    measureHeight.intValue.toDp()
                })
                .background(colors.label))
            //  月
            ColumnPicker(
                modifier = modifier,
                value = month,
                label = { "${it}月" },
                range = 1..12,
                onValueChange = {
                    onChange(1, it)
                }
            )
            Box(modifier = Modifier
                .width(2.dp)
                .height(with(LocalDensity.current){
                    measureHeight.intValue.toDp()
                })
                .background(colors.label))
            //  日
            val lastDay = getLastDay(year, month)
            ColumnPicker(
                modifier = modifier,
                value = min(day,lastDay),
                label = { "${it}日" },
                range = 1..lastDay,
                onValueChange = {
                    onChange(2, it)
                }
            )
        }
    }
}

/**
 * 根据年月, 获取天数
 */
private fun getLastDay(year: Int, month: Int): Int {
    return when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        else -> {
            // 百年: 四百年一闰年;  否则: 四年一闰年;
            if (year % 100 == 0) {
                if (year % 400 == 0) {
                    29
                } else {
                    28
                }
            } else {
                if (year % 4 == 0) {
                    29
                } else {
                    28
                }
            }
        }
    }
}