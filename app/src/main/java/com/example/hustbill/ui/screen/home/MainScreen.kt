package com.example.hustbill.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.hustbill.ui.theme.Gap
import com.example.hustbill.ui.theme.colors
import com.example.hustbill.ui.widgets.TextHeader

@Composable
fun MainScreen(
    horizonPadding:Dp
){
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(horizontal = horizonPadding)
    ){
        TextHeader(modifier = Modifier.fillMaxWidth()
            .padding(vertical = Gap.Big),
            text = "Home")
    }
}