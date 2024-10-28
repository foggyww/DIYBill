package com.example.hustbill.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsIgnoringVisibility
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.hustbill.ui.theme.AppTypography
import com.example.hustbill.ui.theme.colors

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TitleSpacer(color: Color = colors.titleBar) {
    Spacer(
        modifier = Modifier
            .background(color)
            .fillMaxWidth()
            .padding(
                WindowInsets.statusBarsIgnoringVisibility.asPaddingValues()
            )
    )
}

@Composable
fun TextHeader(
    modifier: Modifier,
    text:String
){
    Row(modifier = modifier){
        Text(text, color = colors.textPrimary,
            style = AppTypography.large,
            fontWeight = FontWeight.W600)
    }
}