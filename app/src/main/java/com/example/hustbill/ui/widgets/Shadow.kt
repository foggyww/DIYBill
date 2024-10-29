package com.example.hustbill.ui.widgets


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.hustbill.ui.theme.CardShapes
import com.example.hustbill.ui.theme.colors


/**
 * 绘制阴影范围
 * [top] 顶部范围
 * [start] 开始范围
 * [bottom] 底部范围
 * [end] 结束范围
 * Create empty Shadow elevation
 */
open class ShadowElevation(
    val top: Dp = 0.dp,
    val start: Dp = 0.dp,
    val bottom: Dp = 0.dp,
    val end: Dp = 0.dp
){
    companion object : ShadowElevation()
}

/**
 * 绘制阴影内侧间距
 * @param horizontal 横向
 * @param vertical 纵向
 * @return ShadowElevation
 */
@Stable
fun ShadowElevation.padding(
    horizontal: Dp = 0.dp,
    vertical: Dp = 0.dp
): ShadowElevation {
    return ShadowElevation(
        top = horizontal,
        start = vertical,
        bottom = horizontal,
        end = vertical,
    )
}

/**
 * 绘制阴影所有范围
 * @param elevation 圆角
 * @return ShadowElevation
 */
fun ShadowElevation.all(
    elevation: Dp = 0.dp,
): ShadowElevation {
    return ShadowElevation(
        top = elevation,
        start = elevation,
        bottom = elevation,
        end = elevation,
    )
}



class ShadowShape(
    val topStart: Dp = 0.dp,
    val bottomStart: Dp = 0.dp,
    val topEnd: Dp = 0.dp,
    val bottomEnd: Dp = 0.dp
)

fun ShadowShape.padding(
    topAll: Dp = 0.dp,
    bottomAll: Dp = 0.dp
): ShadowShape {
    return ShadowShape(
        topStart = topAll,
        bottomStart = bottomAll,
        topEnd = topAll,
        bottomEnd = bottomAll,
    )
}

fun ShadowShape.all(
    elevation: Dp = 0.dp,
): ShadowShape {
    return ShadowShape(
        topStart = elevation,
        bottomStart = elevation,
        topEnd = elevation,
        bottomEnd = elevation,
    )
}

@Composable
fun ShadowLayout(
    modifier: Modifier = Modifier,
    shadowColor : Color = colors.shadow,
    elevation: ShadowElevation = ShadowElevation(),
    shape: Dp = 10.dp,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    alpha: Float = 0.2f,
    verticalArrangement:Arrangement.Vertical = Arrangement.Top,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(
                top = elevation.top,
                bottom = elevation.bottom,
                start = elevation.start,
                end = elevation.end
            )
            .drawColoredShadow(
                shadowColor,
                alpha,
                borderRadius = shape,
                shadowRadius = shape,
                offsetX = offsetX,
                offsetY = offsetY,
                roundedRect = false
            )
            .then(modifier)
        ,
        verticalArrangement = verticalArrangement
    ){
        content()
    }
}

/**
 * 绘制基础阴影
 * @param color 颜色
 * @param alpha 颜色透明度
 * @param borderRadius 阴影便捷圆角
 * @param shadowRadius 阴影圆角
 * @param offsetX 偏移X轴
 * @param offsetY 偏移Y轴
 * @param roundedRect 是否绘制圆角就行
 */
fun Modifier.drawColoredShadow(
    color: Color,
    alpha: Float = 0.2f,
    borderRadius: Dp = 0.dp,
    shadowRadius: Dp = 0.dp,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    roundedRect: Boolean = true
) = this.drawBehind {
    /**将颜色转换为Argb的Int类型*/
    val transparentColor = android.graphics.Color.toArgb(color.copy(alpha = .0f).value.toLong())
    val shadowColor = android.graphics.Color.toArgb(color.copy(alpha = alpha).value.toLong())
    /**调用Canvas绘制*/
    this.drawIntoCanvas {
        val paint = Paint()
        paint.color = Color.Transparent
        /**调用底层fragment Paint绘制*/
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.color = transparentColor
        /**绘制阴影*/
        frameworkPaint.setShadowLayer(
            shadowRadius.toPx(),
            offsetX.toPx(),
            offsetY.toPx(),
            shadowColor
        )
        /**形状绘制*/
        it.drawRoundRect(
            0f,
            0f,
            this.size.width,
            this.size.height,
            if (roundedRect) this.size.height / 2 else borderRadius.toPx(),
            if (roundedRect) this.size.height / 2 else borderRadius.toPx(),
            paint
        )
    }
}
