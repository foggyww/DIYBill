package com.example.diybill.ui.widgets

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.diybill.config.Type
import com.example.diybill.ui.theme.AppTypography
import com.example.diybill.ui.theme.CardShapes
import com.example.diybill.ui.theme.Gap
import com.example.diybill.ui.theme.colors
import com.example.diybill.utils.click

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectType(
    modifier: Modifier,
    list: List<Type>,
    selected: List<Type>,
    selectedColor: Color,
    onClick: (Type) -> Unit,
){
    @Composable
    fun CardItem(type: Type, select: Boolean, onClick: (Type) -> Unit) {
        Box(modifier = Modifier
            .height(28.dp)
            .clip(CardShapes.small)
            .click {
                onClick.invoke(type)
            }
            .background(if (select) selectedColor else colors.unfocused)
            .padding(vertical = Gap.Small, horizontal = Gap.Mid),
            contentAlignment = Alignment.Center) {
            Text(type.cnName, style = AppTypography.smallMsg, color = colors.background)
        }
    }

    FlowRow(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Gap.Mid, Alignment.Top),
        horizontalArrangement = Arrangement.spacedBy(Gap.Big, Alignment.Start)
    ) {
        list.forEach {
            CardItem(it, selected.contains(it), onClick)
        }
    }
}