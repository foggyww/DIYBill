package com.example.diybill.ui.screen.home

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.ui.graphics.toArgb
import com.example.diybill.ui.theme.grey1
import com.example.diybill.ui.theme.secondaryColor
import com.example.diybill.utils.toString
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import java.math.BigDecimal

@SuppressLint("ViewConstructor")
class WeekBarChart(context:Context, weekBillList:List<DayBill>) :BarChart(context){
    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT)
        setScaleEnabled(false)
        isDoubleTapToZoomEnabled = false
        xAxis.setDrawGridLines(false)
        description.isEnabled = false
        legend.isEnabled = false
        animateXY(500,500)
        xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            axisMinimum = -0.5f
            axisMaximum = weekBillList.size.toFloat()
            setVisibleXRange(-0.1f,6.1f)
            setLabelCount(weekBillList.size)
            setDrawGridLines(false)
            granularity = 1f
            valueFormatter = object :ValueFormatter(){
                override fun getFormattedValue(value: Float): String {
                    if(value.toInt()<weekBillList.size){
                        val date = weekBillList[value.toInt()].date
                        return "${date.day}日"
                    }
                    return ""
                }
            }
            textSize = 10f
            textColor = grey1.toArgb()
            yOffset  = 10f
        }
        axisLeft.apply {
            axisMinimum = 0f
            isEnabled = false
        }
        axisRight.apply {
            axisMinimum = 0f
            isEnabled = false
        }

        val barEntries = weekBillList.mapIndexed { index, dayBillList ->
            BarEntry((index).toFloat(),dayBillList.billList.sumOf { BigDecimal(it.amount).abs() }.toFloat(),dayBillList.date.toString)
        }

        val barDataSet = BarDataSet(barEntries,"").apply {
            valueTextColor = secondaryColor.toArgb()
            color = secondaryColor.toArgb()
            form = Legend.LegendForm.CIRCLE
            valueFormatter = object :ValueFormatter(){
                override fun getFormattedValue(value: Float): String {
                    return "%.2f".format(value)
                }
            }
            valueTextSize = 10f
            highLightColor = secondaryColor.toArgb()
        }
        val barData = BarData(barDataSet).apply {
            barWidth = 0.5f
        }
        data = barData

        invalidate()
    }
}