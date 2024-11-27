package com.example.diybill.ui.screen.support

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.diybill.ui.theme.AppTypography
import com.example.diybill.ui.theme.Gap
import com.example.diybill.ui.theme.ImageSize
import com.example.diybill.ui.widgets.EasyImage
import com.example.diybill.ui.widgets.TextHeader
import com.example.diybill.ui.widgets.TitleCard

@Composable
fun SupportScreen(
    contentPadding: PaddingValues,
    vm:SupportViewModel = viewModel(),
)
{
    val list = remember {
        supportList
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        TextHeader(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Gap.Big),
            text = "Support"
        )
        TitleCard(
            modifier = Modifier.fillMaxWidth(),
            title = "已支持"
        ) {

            @Composable
            fun SupportRow(name:String,@DrawableRes icon:Int){
                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically){
                    Text(text = name, style = AppTypography.smallMsg)
                    Spacer(modifier = Modifier.weight(1f))
                    EasyImage(src = icon,
                        contentDescription = "icon",
                        modifier = Modifier.size(ImageSize.Mid))
                }
            }
            list.forEach {
                SupportRow(it.name,it.icon)
            }

            Row {

            }
        }
    }
}