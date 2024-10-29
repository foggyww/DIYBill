package com.example.hustbill.ui.screen.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hustbill.R
import com.example.hustbill.ui.theme.AppTypography
import com.example.hustbill.ui.theme.CardShapes
import com.example.hustbill.ui.theme.Gap
import com.example.hustbill.ui.theme.RoundedShapes
import com.example.hustbill.ui.theme.colors
import com.example.hustbill.ui.widgets.CommonCard
import com.example.hustbill.ui.widgets.CommonText
import com.example.hustbill.ui.widgets.EasyImage
import com.example.hustbill.ui.widgets.TextHeader
import com.example.hustbill.ui.widgets.TitleCard
import com.example.hustbill.utils.toInt
import com.example.hustbill.utils.toString

@Composable
fun MainScreen(
    horizonPadding:Dp,
    vm:MainViewModel = viewModel()
){
    LaunchedEffect(Unit) {
        vm.init()
    }

    val state by vm.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(horizontal = horizonPadding)
    ){
        TextHeader(modifier = Modifier.fillMaxWidth()
            .padding(vertical = Gap.Big),
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
    val nowConsume = -billState.total
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
        @Composable
        fun item(
            text:String,
            type:String,
            amount:String
        ){
            Row(modifier = Modifier.fillMaxWidth()
                .height(40.dp),
                verticalAlignment = Alignment.CenterVertically){
                Box(modifier = Modifier.size(30.dp)
                    .clip(RoundedShapes.large)
                    .background(colors.secondary),
                    contentAlignment = Alignment.Center){
                    Text(text=type.substring(0..0), color = colors.background, style = AppTypography.smallMsg)
                }
                Spacer(modifier = Modifier.width(Gap.Mid))
                Text(text=text, color = colors.textSecondary, style = AppTypography.smallMsg)
                Spacer(modifier = Modifier.weight(1f))
                Text(text=amount, color = colors.secondary, style = AppTypography.smallMsg)
            }
        }
        Column(modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Gap.Large)){
            for (bill in dayBill.billList){
                item(bill.name,bill.type.cnName,bill.amount)
            }
        }
    }
}
