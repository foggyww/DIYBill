package com.example.diybill.ui.widgets

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

@Composable
fun CommonText(
    modifier: Modifier=Modifier,
    text:String,
    style: TextStyle,
    color:Color
){
    Text(text = text,
        modifier = modifier,
        style = style,
        color = color,
        textAlign = TextAlign.Center,)
}