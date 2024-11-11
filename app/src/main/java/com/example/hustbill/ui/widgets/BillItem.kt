package com.example.hustbill.ui.widgets

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.hustbill.config.OutlayType
import com.example.hustbill.db.Bill
import com.example.hustbill.ui.theme.AppTypography
import com.example.hustbill.ui.theme.Gap
import com.example.hustbill.ui.theme.RoundedShapes
import com.example.hustbill.ui.theme.colors


@Composable
fun BillItem(
    bill: Bill
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(RoundedShapes.large)
                .background(colors.secondary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = bill.type.cnName.substring(0..0),
                color = colors.background,
                style = AppTypography.smallMsg
            )
        }
        Spacer(modifier = Modifier.width(Gap.Mid))
        Text(text = bill.name, color = colors.textSecondary, style = AppTypography.smallMsg,
            modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(Gap.Mid))
        Text(text = if(bill.type.isOutlay) "-${bill.amount}" else bill.amount, color = colors.secondary, style = AppTypography.smallMsg)
    }
}