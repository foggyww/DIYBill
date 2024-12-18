package com.example.diybill.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.times
import com.example.diybill.AppRoute
import com.example.diybill.db.Bill
import com.example.diybill.ui.provider.LocalNav
import com.example.diybill.ui.screen.support.supportList
import com.example.diybill.ui.theme.AppTypography
import com.example.diybill.ui.theme.CardShapes
import com.example.diybill.ui.theme.Gap
import com.example.diybill.ui.theme.ImageSize
import com.example.diybill.ui.theme.RoundedShapes
import com.example.diybill.ui.theme.colors
import com.example.diybill.utils.clickNoRepeat

@Composable
fun BillItem(
    bill: Bill,
) {
    val nav = LocalNav.current
    val density = LocalDensity.current
    Column(
        modifier = Modifier
            .clip(CardShapes.extraSmall)
            .clickNoRepeat {
                nav.navigate(AppRoute.UPDATE + "?id=" + bill.id)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
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
            Text(
                modifier = Modifier.weight(1f),
                text = bill.name, color = colors.textSecondary, style = AppTypography.smallMsg,
            )
            Spacer(modifier = Modifier.width(Gap.Mid))
            supportList.find { s -> s.name == bill.source }?.let { s ->
                EasyImage(
                    src = s.icon,
                    modifier = Modifier.size(ImageSize.Mid),
                    contentDescription = "icon"
                )
            } ?: Spacer(modifier = Modifier.size(ImageSize.Mid))
            Spacer(modifier = Modifier.width(Gap.Mid))
            Text(
                modifier = Modifier.width(68.dp),
                textAlign = TextAlign.End,
                text = if (bill.type.isOutlay) "-${bill.amount}" else bill.amount,
                color = colors.secondary,
                style = AppTypography.smallMsg
            )





        }
        if (bill.urls.isNotEmpty()) {
            Spacer(modifier = Modifier.height(Gap.Small))
            BoxWithConstraints {
                val width = maxWidth
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Gap.Small, Alignment.Start)
                ) {
                    bill.urls.forEach {
                        Box(
                            modifier = Modifier
                                .width((width - 2 * Gap.Small) / 3)
                                .background(colors.label, CardShapes.medium)
                                .aspectRatio(1f)
                                .clip(CardShapes.extraSmall),
                            contentAlignment = Alignment.Center
                        ) {
                            MemCacheImage(
                                modifier = Modifier.fillMaxSize(),
                                path = it
                            )
                        }
                    }
                }
            }
        }
    }
}