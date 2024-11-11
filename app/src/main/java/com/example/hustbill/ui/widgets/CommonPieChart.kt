package com.example.hustbill.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter

@SuppressLint("ViewConstructor")
class CommonPieChart(
    context: Context,
    holeColor: Color,
) : PieChart(context) {
    companion object {
        val colorList = listOf(
            Color(99, 178, 238, 255),
            Color(118, 218, 145, 255),
            Color(248, 203, 127, 255),
            Color(248, 149, 136, 255),
            Color(124, 214, 207, 255),
            Color(145, 146, 171, 255),
            Color(120, 152, 225, 255),

            )
        const val MAX_SHOW = 6
    }

    fun setEntries(
        entries: List<PieEntry>,
    ) {
        val showEntries = entries.sortedBy { it.value }.take(6)
        val otherEntries = entries - showEntries.toSet()
        val otherEntry = PieEntry(otherEntries.sumOf { it.value.toDouble() }.toFloat(), "其他")
        val colorsIntArray = colorList.map { it.toArgb() }.toIntArray()
        val dataSet = if (entries.size > 6) {

            PieDataSet(showEntries + otherEntry, "")
        } else {
            PieDataSet(showEntries, "")
        }
        dataSet.apply {
            sliceSpace = 0f
            setColors(colorsIntArray, 255) // 设置颜色和透明度
            valueLinePart1OffsetPercentage = 80f
            valueLinePart1Length = 0.3f  // 设置折线第1部分的长度
            valueLinePart2Length = 0.3f  // 设置折线第2部分的长度
            xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
            yValuePosition =
                PieDataSet.ValuePosition.OUTSIDE_SLICE  // 在饼图外部显示百分比
            valueLineWidth = 2f  // 设置折线宽度
            valueLineColor = Color.Black.toArgb() // 使用数据段颜色作为折线颜色
            sliceSpace = 1f
        }
        val pieData = PieData(dataSet)
        pieData.setValueFormatter(PercentFormatter(this)) // 使用自定义的格式化器
        pieData.setValueTextSize(14f)
        pieData.setValueTextColor(Color.Gray.toArgb())
        data = pieData
    }

    init {
        val colorsIntArray = colorList.map { it.toArgb() }.toIntArray()
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        //初始化组件
        setUsePercentValues(true)
        description.isEnabled = false
        setDragDecelerationFrictionCoef(0.95f)
        //是否设置中心文字
        setDrawCenterText(true)
        setExtraOffsets(20f, 0f, 20f, 0f)
        //设置空心图及空心颜色
        isDrawHoleEnabled = true
        setHoleColor(Color.White.toArgb())
        //设置圆环信息
        setTransparentCircleColor(Color.White.toArgb())
        setTransparentCircleAlpha(110)
        holeRadius = 55f
        transparentCircleRadius = 65f
        setHoleColor(holeColor.toArgb())
        setRotationAngle(0f)

        //触摸旋转
        isRotationEnabled = true
        //选中变大
        isHighlightPerTapEnabled = true
        highlightValues(null)
        notifyDataSetChanged()
        invalidate()
        setHoleColor(holeColor.toArgb())
        //默认动画
        animateY(1400, Easing.EaseInOutQuad)

        //设置图例
        legend.apply {
            verticalAlignment = Legend.LegendVerticalAlignment.TOP
            horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            setDrawInside(true)
            isEnabled = false
        }

        //输入图例标签样式
        setEntryLabelColor(Color.Black.toArgb())
        setEntryLabelTextSize(14f)
    }
}