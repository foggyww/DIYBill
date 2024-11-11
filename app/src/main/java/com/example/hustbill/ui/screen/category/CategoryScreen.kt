package com.example.hustbill.ui.screen.category

import android.text.InputType
import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hustbill.R
import com.example.hustbill.config.IncomeType
import com.example.hustbill.config.OutlayType
import com.example.hustbill.config.Type
import com.example.hustbill.ui.theme.AppTypography
import com.example.hustbill.ui.theme.CardShapes
import com.example.hustbill.ui.theme.CardShapesTopHalf
import com.example.hustbill.ui.theme.Gap
import com.example.hustbill.ui.theme.colors
import com.example.hustbill.ui.widgets.BillItem
import com.example.hustbill.ui.widgets.CommonCard
import com.example.hustbill.ui.widgets.EasyImage
import com.example.hustbill.ui.widgets.SelectType
import com.example.hustbill.ui.widgets.TextHeader
import com.example.hustbill.ui.widgets.TitleCard
import com.example.hustbill.ui.widgets.rememberChooseDateDialog
import com.example.hustbill.utils.DateRange
import com.example.hustbill.utils.click
import com.example.hustbill.utils.toString
import com.github.mikephil.charting.charts.PieChart

@Composable
fun CategoryScreen(
    contentPadding: PaddingValues,
    vm: CategoryViewModel = viewModel(),
) {
    val context = LocalContext.current
    LaunchedEffect(context) {
        vm.init()
    }
    val state by vm.state.collectAsState()

    val showDateRange = remember{
        mutableStateOf(false)
    }
    CategoryDialog(showDateRange, dateRange = state.dateRange){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        TextHeader(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Gap.Big),
            text = "Category"
        )
        PieChartItem(billState = state.billState)
        CommonCard(
            modifier = Modifier.fillMaxWidth(),
            shape = CardShapesTopHalf.medium
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(Gap.Large, Alignment.Top),
            ) {
                item(key = -1) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(Gap.Big, Alignment.Top)
                    ) {
                        Text(text = "支出标签", style = AppTypography.smallTitle)
                        SelectLabelItem(
                            OutlayType.entries.toList(),
                            state.labelState,
                            colors.primary,
                        )
                        Text(text = "收入标签", style = AppTypography.smallTitle)
                        SelectLabelItem(
                            IncomeType.entries.toList(),
                            state.labelState,
                            colors.secondary
                        )
                        Text(text = "日期选择", style = AppTypography.smallTitle)
                        SelectDateItem(state.dateRange) {
                            showDateRange.value = true
                        }
                        Text(
                            text = "筛选金额：${state.totalAmount}",
                            style = AppTypography.smallMsg,
                            color = colors.primary
                        )
                    }
                }
                items(items = state.billState.billList, key = { it.id }) {
                    BillItem(it)
                }
            }
        }
    }
    }
}

@Composable
fun PieChartItem(
    billState: BillState,
) {
    val holeColor = colors.background
    AndroidView(modifier = Modifier
        .fillMaxWidth()
        .height(240.dp+(Gap.Big*2))
        .padding(vertical = Gap.Big),
        factory = { context ->
            CategoryPieChartFactory.getPieChart(
                context,
                holeColor,
                billState
            )
        },
        update = {
            CategoryPieChartFactory.setPieChart(it,billState)
        })

}

@Composable
private fun SelectLabelItem(
    list: List<Type>, labelState: LabelState,
    color: Color,
    vm: CategoryViewModel = viewModel(),
) {
    SelectType(
        modifier = Modifier.fillMaxWidth(),
        list = list,
        selected = labelState.labelSelected,
        selectedColor = color
    ) {
        val newList = if (labelState.labelSelected.contains(it)) {
            labelState.labelSelected.filter { label ->
                label != it
            }
        } else {
            labelState.labelSelected + it
        }
        vm.changeLabel(
            labelState.copy(labelSelected = newList)
        )
    }
}

@Composable
fun SelectDateItem(dateRange: DateRange,onClick:()->Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CardShapes.small)
            .background(colors.secondary)
            .padding(vertical = Gap.Small),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = dateRange.toString, style = AppTypography.smallMsg, color = colors.background)
        Spacer(modifier = Modifier.width(Gap.Small))
        EasyImage(
            src = R.drawable.calendar,
            modifier = Modifier
                .size(20.dp)
                .click {
                    onClick()
                },
            contentDescription = "选择日历"
        )
    }
}

@Composable
fun CategoryDialog(
    showDateRange:MutableState<Boolean>,
    dateRange: DateRange,
    vm: CategoryViewModel = viewModel(),
    content:@Composable ()->Unit
){
    val context = LocalContext.current
    val dateDialog = rememberChooseDateDialog(
        positive = {
            vm.changeDateRange(it)
            showDateRange.value = false
        },
        negative = {
            showDateRange.value = false
        },
        initDateRange = dateRange
    )

    dateDialog.Build(
        show = showDateRange.value,
        properties = DialogProperties(),
        onDismissRequest = {
            showDateRange.value = false
        }
    ) {
        content()
    }
}