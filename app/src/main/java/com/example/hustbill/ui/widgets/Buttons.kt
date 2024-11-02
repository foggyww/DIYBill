package com.example.hustbill.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.example.hustbill.ui.theme.CardShapes
import com.example.hustbill.ui.theme.Gap
import com.example.hustbill.ui.theme.colors

@Composable
fun FillButton(
    modifier: Modifier = Modifier,
    color: Color = colors.background,
    textColor: Color = colors.textPrimary,
    style: TextStyle,
    text: String,
    clickEnable: Boolean = true,
    onClick: () -> Unit,
) {
    Box(
        modifier =
        if (clickEnable) Modifier
            .clip(CardShapes.small)
            .clickable { onClick() }
            .background(color)
            .padding(vertical = Gap.Small, horizontal = Gap.Big)
            .then(modifier)
        else
            Modifier
                .clip(CardShapes.small)
                .background(color)
                .padding(vertical = Gap.Small, horizontal = Gap.Big)
                .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            style = style
        )
    }
}