package com.bingyan.focuscat.ui.widgets.background

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.diybill.ui.theme.colors

@Composable
fun MainBackground(
    padding : Dp = 0.dp,
    content : @Composable ColumnScope.()->Unit
){
    Surface(modifier = Modifier.fillMaxSize()
        .background(colors.background)){
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
    padding : Dp = 0.dp,
    content : @Composable ()->Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = colors.geryBackground
            )
            .padding(padding)
    ) {
        content()
    }
}