package com.example.diybill.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.diybill.ui.provider.toast
import com.example.diybill.ui.theme.AppTypography
import com.example.diybill.ui.theme.Gap
import com.example.diybill.ui.theme.colors
import com.example.diybill.ui.theme.grey3
import com.example.diybill.utils.Date
import com.example.diybill.utils.DateRange
import com.example.diybill.utils.toInt
import com.example.diybill.utils.toString

@Composable
fun rememberChooseDateDialog(
    positive: (DateRange) -> Unit ,
    negative: () -> Unit ,
    initDateRange: DateRange,
    title: String = "请选择起止日期",
):AppDialog{
    val startDate = remember {
        mutableStateOf(initDateRange.start)
    }
    val endDate = remember {
        mutableStateOf(initDateRange.end)
    }
    val context = LocalContext.current
    val chooseStart = remember {
        mutableStateOf(true)
    }
    val chooseDate = if(chooseStart.value){
        startDate.value
    }else{
        endDate.value
    }
    return remember(positive,negative,initDateRange){
        AppDialog()
            .apply {
                withSpace(8.dp)
                withTitle(
                    title
                )
                withView {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        @Composable
                        fun chooseTab(
                            text: String,
                            value: String,
                            selected: Boolean,
                            onSelect: () -> Unit,
                        ) {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelect()
                                }
                                .padding(Gap.Mid),
                                verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .height(20.dp)
                                        .width(2.dp)
                                        .background(colors.secondary)
                                )
                                Spacer(modifier = Modifier.width(Gap.Big))
                                Text(
                                    text, style = AppTypography.smallMsg,
                                    color = if (!selected) grey3 else colors.textPrimary
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    value, style = AppTypography.smallMsg,
                                    color = if (!selected) grey3 else colors.textPrimary
                                )
                            }
                        }

                        chooseTab("起始日期", startDate.value.toString, chooseStart.value) {
                            chooseStart.value = true
                        }

                        chooseTab("结束日期", endDate.value.toString, !chooseStart.value) {
                            chooseStart.value = false
                        }
                        DatePicker(chooseDate.year,chooseDate.month,chooseDate.day){ y, m, d ->
                            if (chooseStart.value) {
                                startDate.value = Date(y, m, d)
                            } else {
                                endDate.value = Date(y, m, d)
                            }
                        }
                    }
                }
                positive(text = "确定") {
                    if(startDate.value.toInt<=endDate.value.toInt){
                        positive(DateRange(startDate.value, endDate.value))
                    }else{
                        context.toast("非法的日期")
                    }
                    true
                }

                negative(text = "取消") {
                    negative()
                    true
                }
            }
    }
}