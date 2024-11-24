package com.example.diybill.ui.screen.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.diybill.R
import com.example.diybill.db.IOCallback
import com.example.diybill.ui.provider.toast
import com.example.diybill.ui.theme.AppTypography
import com.example.diybill.ui.theme.CardShapes
import com.example.diybill.ui.theme.Gap
import com.example.diybill.ui.theme.RoundedShapes
import com.example.diybill.ui.theme.colors
import com.example.diybill.ui.widgets.BillItem
import com.example.diybill.ui.widgets.CommonCard
import com.example.diybill.ui.widgets.CommonText
import com.example.diybill.ui.widgets.EasyImage
import com.example.diybill.ui.widgets.TextHeader
import com.example.diybill.ui.widgets.TitleCard
import com.example.diybill.utils.click
import com.example.diybill.utils.toInt
import com.example.diybill.utils.toString
import com.example.diybill.web.WebUtil
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    contentPadding:PaddingValues,
    vm:MainViewModel = viewModel()
){
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        vm.init()
    }
    LaunchedEffect(Unit) {
        vm.uploadAutoRecord()
    }

    val state by vm.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(contentPadding)
    ){
        TextHeader(modifier = Modifier.fillMaxWidth()
            .padding(vertical = Gap.Big)
            .click {
                vm.viewModelScope.launch {
                    WebUtil.requestChatBot(emptyList(), ioCallback = IOCallback(
                        onSuccess = {
                            context.toast(it)
                        }
                    ))
                }
            },
            text = "Home")
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(Gap.Large)
        ){
            item(key = 1){
                MonthCard(state.billState,vm.config)
            }
            item(key = 2){
                WeekBarCard(state.billState)
            }
            items(state.billState.dayBillList, key = {
                it.date.toInt
            }){
                DayBillCard(dayBill = it)
            }
        }

    }
}

@Composable
fun MonthCard(
    billState: BillState,
    config: MainConfig,
){
    val budget = config.budge
    val nowConsume = billState.total
    TitleCard(
        modifier = Modifier.fillMaxWidth(),
        title = "本月数据分析",
        verticalArrangement = Arrangement.spacedBy(Gap.Big,Alignment.Top)
    ) {
        LinearProgressIndicator(
            progress = {
                nowConsume.toFloat()/budget
            },
            modifier = Modifier.fillMaxWidth()
                .height(10.dp)
                .clip(CardShapes.large),
            trackColor = colors.primary,
            color = colors.secondary
        )
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically){
            @Composable
            fun capsule(text:String,amount:Int,
                        color:Color,
                        @DrawableRes src:Int){
                EasyImage(modifier = Modifier.size(15.dp)
                    .clip(RoundedShapes.large),
                    src = src,
                    contentDescription = "圆圈")
                Spacer(modifier = Modifier.width(Gap.Small))
                CommonText(text =text, color = colors.textSecondary, style = AppTypography.smallMsg)
                CommonText(text = amount.toString(), color = color, style = AppTypography.smallMsg)
                CommonText(text="元", color = colors.textSecondary, style = AppTypography.smallMsg)
            }
            capsule("本月支出:",nowConsume, colors.secondary,R.drawable.circle_pink)
            Spacer(modifier = Modifier.weight(1f))
            capsule("本月预算:",budget-nowConsume, colors.primary,R.drawable.circle_yellow)
        }
    }
}


@Composable
fun WeekBarCard(
    billState: BillState,
){
    CommonCard(
        modifier = Modifier.fillMaxWidth(),
        horizonPadding = Gap.Mid,
    ) {
        Text(modifier = Modifier.padding(horizontal = Gap.Big),text = "七日支出", style = AppTypography.smallTitle)
        if(billState.dayBillList.isNotEmpty()){
            AndroidView(
                modifier = Modifier.fillMaxWidth()
                    .height(150.dp),
                factory = { context->
                    WeekBarChart(context,billState.getWeekBillList())
                }
            )
        }
    }
}

@Composable
fun DayBillCard(
    dayBill: DayBill
){
    TitleCard(modifier = Modifier.fillMaxWidth(),
        title = dayBill.date.toString,
        verticalArrangement = Arrangement.spacedBy(Gap.Big,Alignment.Top)) {
        Column(modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Gap.Large)){
            for (bill in dayBill.billList){
                BillItem(bill)
            }
        }
    }
}
