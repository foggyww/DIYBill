package com.bingyan.focuscat.ui.widgets.background

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.example.hustbill.ui.theme.Gap
import com.example.hustbill.ui.theme.colors
import com.example.hustbill.utils.click

@Composable
fun MainBackground(
    padding : Dp = Gap.Mid,
    content : @Composable ColumnScope.()->Unit
){
    Surface(modifier = Modifier.fillMaxSize()
        .background(colors.background)
        .click {}){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
                .padding(padding)
        ) {
            content()
        }
    }
}

@Composable
fun SettingBackground(
    padding : Dp = Gap.Mid,
    content : @Composable ()->Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = colors.heavyBackground
            )
            .padding(padding)
    ) {
        content()
    }
}