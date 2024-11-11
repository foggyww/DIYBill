package com.example.hustbill.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.hustbill.ui.theme.AppTypography
import com.example.hustbill.ui.theme.CardShapes
import com.example.hustbill.ui.theme.Gap
import com.example.hustbill.ui.theme.colors

@Composable
fun CommonCard(
    modifier: Modifier,
    verticalPadding: Dp = Gap.Large,
    horizonPadding: Dp = Gap.Large,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(Gap.Big,Alignment.Top),
    shape: CornerBasedShape = CardShapes.medium,
    content: @Composable ColumnScope.() -> Unit,
) {
    ShadowLayout(
        modifier = modifier
            .clip(shape)
            .background(colors.background)
            .padding(horizontal = horizonPadding, vertical = verticalPadding),
        elevation = ShadowElevation(0.5.dp, 0.5.dp, 0.5.dp, 0.5.dp),
        verticalArrangement = verticalArrangement,
        alpha = 0.1f
    ) {
        content()
    }
}

@Composable
fun TitleCard(
    modifier: Modifier,
    title: String,
    verticalPadding: Dp = Gap.Large,
    horizonPadding: Dp = Gap.Large,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(Gap.Big,Alignment.Top),
    content: @Composable ColumnScope.() -> Unit,
) {
    CommonCard(
        modifier, horizonPadding = horizonPadding, verticalPadding = verticalPadding,
        verticalArrangement = verticalArrangement
    ) {
        Text(text = title, style = AppTypography.smallTitle)
        content()
    }
}

@Composable
fun InputCard(
    modifier: Modifier,
    color:Color = colors.unfocusedSecondary,
    content:@Composable RowScope.() -> Unit
){
    Row(modifier = Modifier
        .clip(CardShapes.small)
        .border(0.5.dp, colors.lightBorder, CardShapes.small)
        .background(color)
        .then(modifier),
        verticalAlignment = Alignment.CenterVertically){
        content()
    }
}