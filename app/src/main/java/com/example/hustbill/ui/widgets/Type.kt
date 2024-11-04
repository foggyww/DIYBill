package com.example.hustbill.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.hustbill.ui.theme.AppTypography
import com.example.hustbill.ui.theme.CardShapes
import com.example.hustbill.ui.theme.Gap
import com.example.hustbill.ui.theme.colors
import com.example.hustbill.utils.click

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectType(
    list: List<String>,
    selected: List<String>,
    onClick: (String) -> Unit,
){
    @Composable
    fun CardItem(text: String, select: Boolean, onClick: (String) -> Unit) {
        Box(modifier = Modifier
            .height(28.dp)
            .clip(CardShapes.medium)
            .click {
                onClick.invoke(text)
            }
            .background(if (select) colors.secondary else colors.unfocused)
            .padding(vertical = Gap.Small, horizontal = Gap.Mid)) {
            Text(text, style = AppTypography.smallMsg, color = colors.background)
        }
    }

    FlowRow(
        verticalArrangement = Arrangement.spacedBy(Gap.Mid, Alignment.Top),
        horizontalArrangement = Arrangement.spacedBy(Gap.Big, Alignment.Start)
    ) {
        list.forEach {
            CardItem(it, selected.contains(it), onClick)
        }
    }
}