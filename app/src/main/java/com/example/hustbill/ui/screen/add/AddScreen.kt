package com.example.hustbill.ui.screen.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.hustbill.config.Config
import com.example.hustbill.ui.theme.AppTypography
import com.example.hustbill.ui.theme.CardShapes
import com.example.hustbill.ui.theme.CardShapesTopHalf
import com.example.hustbill.ui.theme.Gap
import com.example.hustbill.ui.theme.colors
import com.example.hustbill.ui.widgets.CommonCard
import com.example.hustbill.ui.widgets.InputCard
import com.example.hustbill.ui.widgets.PlainTextField
import com.example.hustbill.ui.widgets.ShadowElevation
import com.example.hustbill.ui.widgets.ShadowLayout
import com.example.hustbill.ui.widgets.TextHeader
import com.example.hustbill.ui.widgets.TitleCard
import com.example.hustbill.ui.widgets.TitleSpacer
import com.gigamole.composeshadowsplus.rsblur.rsBlurShadow
import java.math.BigDecimal

@Composable
fun AddScreen(
    contentPadding: PaddingValues,
) {

    val name = remember {
        mutableStateOf("")
    }
    val amount = remember {
        mutableStateOf("")
    }
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            TitleSpacer()
            TextHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Gap.Big),
                text = "Bills"
            )
            Row(modifier = Modifier.fillMaxWidth()
                .zIndex(0f)){
                ShadowLayout(
                    modifier = Modifier
                        .weight(0.3f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                        .background(colors.background)
                        .zIndex(1f),
                    verticalArrangement = Arrangement.spacedBy(Gap.Big,Alignment.CenterVertically),
                    alpha = 0.1f,
                ) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center){
                        Text(text = "支出账单", style = AppTypography.smallTitle,
                            color = colors.secondary)
                    }
                }
                ShadowLayout(
                    modifier = Modifier
                        .weight(0.3f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(topStart =10.dp, topEnd = 10.dp))
                        .background(colors.background)
                        .zIndex(0.5f),
                    verticalArrangement =Arrangement.spacedBy(Gap.Big,Alignment.CenterVertically),
                    alpha = 0.1f
                ) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center){
                        Text(text = "收入账单", style = AppTypography.smallTitle,
                            color = colors.unfocused)
                    }
                }
                Spacer(modifier = Modifier.weight(0.4f))
            }
            Column(
                modifier = Modifier
                    .rsBlurShadow(
                        radius = 10.dp,
                        shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp),
                        color = colors.unfocused.copy(alpha = 0.2f),
                        offset = DpOffset(0.dp,10.dp)
                    )
//                    .rsBlurShadow(
//                        radius = 0.dp,
//                        shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp),
//                        color = colors.unfocused.copy(alpha = 0.2f),
//                        offset = DpOffset((-10).dp,10.dp)
//                    )
                    .clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
                    .background(colors.background)
                    .padding(horizontal = Gap.Large, vertical =Gap.Large)
                    .zIndex(1f),
                verticalArrangement = Arrangement.spacedBy(Gap.Big,Alignment.Top),
            ) {
                @Composable
                fun ItemInput(
                    text: String,
                    type: KeyboardType,
                    value: MutableState<String>,
                    onValueChange: (String) -> Unit,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text, style = AppTypography.smallMsg)
                        Spacer(modifier = Modifier.width(Gap.Big))
                        InputCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(28.dp)
                                .padding(horizontal = Gap.Mid)
                        ) {
                            PlainTextField(
                                value = value.value,
                                hint = "限制长度为${Config.NAME_MAX_LENGTH}",
                                onValueChange = onValueChange,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = type
                                ),
                                singleLine = true,
                                maxLines = 1,
                                textStyle = AppTypography.inputMsg,
                                hintColor = colors.unfocused
                            )
                        }
                    }
                }

                @Composable
                fun ItemSelect(

                ) {

                }
                ItemInput("名称",
                    KeyboardType.Text,
                    name,
                    onValueChange = {
                        if (it.length <= Config.NAME_MAX_LENGTH) {
                            name.value = it
                        }
                    }
                )
                ItemInput("金额",
                    KeyboardType.Number,
                    amount,
                    onValueChange = {
                        try {
                            val bd = BigDecimal(it).setScale(2)
                            if (bd < BigDecimal("10000.00")
                                && bd >= BigDecimal("0.00")
                            ) {
                                amount.value = it
                            }
                        } catch (_: Throwable) {
                            if (it == "") {
                                amount.value = it
                            }
                        }
                    })
            }

        }
    }

}