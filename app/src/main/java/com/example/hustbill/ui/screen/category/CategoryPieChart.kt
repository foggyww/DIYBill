package com.example.hustbill.ui.screen.category

import android.content.Context
import androidx.compose.ui.graphics.Color
import com.example.hustbill.ui.widgets.CommonPieChart
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieEntry

object CategoryPieChartFactory {
    fun getPieChart(
        context: Context,
        holeColor: Color,
        billState: BillState,
    ):CommonPieChart{
        val map = billState.billList.groupBy {
            it.type
        }
        val entries: MutableList<PieEntry> = mutableListOf()
        map.forEach { (t, u) ->
            entries.add(PieEntry(u.sumOf { it.amount.toBigDecimal() }.toFloat(), t.cnName))
        }
        val pieChart = CommonPieChart(context,holeColor)
        pieChart.setEntries(entries.toList())
        return CommonPieChart(context,holeColor)
    }

    fun setPieChart(
        pieChart: CommonPieChart,
        billState: BillState
    ){
        val map = billState.billList.groupBy {
            it.type
        }
        val entries: MutableList<PieEntry> = mutableListOf()
        map.forEach { (t, u) ->
            entries.add(PieEntry(u.sumOf { it.amount.toBigDecimal() }.toFloat(), t.cnName))
        }
        pieChart.setEntries(entries)
        pieChart.animateY(1400, Easing.EaseInOutQuad)
        pieChart.invalidate()
    }

}