package com.example.hustbill.ui.screen.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hustbill.R
import com.example.hustbill.config.OutlayType
import com.example.hustbill.config.Config
import com.example.hustbill.config.IncomeType
import com.example.hustbill.config.Type
import com.example.hustbill.config.toOutlayType
import com.example.hustbill.config.toIncomeType
import com.example.hustbill.config.toStringList
import com.example.hustbill.db.Bill
import com.example.hustbill.db.BillHelper
import com.example.hustbill.db.IOCallback
import com.example.hustbill.pop
import com.example.hustbill.ui.provider.LocalNav
import com.example.hustbill.ui.provider.toast
import com.example.hustbill.ui.theme.AppTypography
import com.example.hustbill.ui.theme.CardShapes
import com.example.hustbill.ui.theme.Gap
import com.example.hustbill.ui.theme.colors
import com.example.hustbill.ui.widgets.EasyImage
import com.example.hustbill.ui.widgets.FillButton
import com.example.hustbill.ui.widgets.InputCard
import com.example.hustbill.ui.widgets.PlainTextField
import com.example.hustbill.ui.widgets.SelectType
import com.example.hustbill.ui.widgets.ShadowLayout
import com.example.hustbill.ui.widgets.TextHeader
import com.example.hustbill.ui.widgets.TitleSpacer
import com.example.hustbill.utils.click
import com.example.hustbill.utils.getDate
import com.example.hustbill.utils.toString
import com.gigamole.composeshadowsplus.rsblur.rsBlurShadow
import java.math.BigDecimal

@Composable
fun AddScreen(
    contentPadding: PaddingValues,
    vm:AddViewModel = viewModel()
) {

    val name = remember {
        mutableStateOf("")
    }
    val amount = remember {
        mutableStateOf("")
    }
    val state :MutableState<Type> = remember {
        mutableStateOf(OutlayType.Other)
    }
    val date = remember {
        mutableStateOf(getDate())
    }
    val context = LocalContext.current
    val nav = LocalNav.current
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(0f)
            ) {
                @Composable
                fun Item(
                    text: String,
                    selected: Boolean,
                    onClick: () -> Unit,
                ) {
                    ShadowLayout(
                        modifier = Modifier
                            .weight(0.3f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                            .click {
                                onClick()
                            }
                            .background(
                                if (selected) colors.background else colors.background.copy(
                                    alpha = 0.6f
                                )
                            )
                            .zIndex(if (selected) 1f else 0f),
                        verticalArrangement = Arrangement.spacedBy(
                            Gap.Big,
                            Alignment.CenterVertically
                        ),
                        alpha = 0.1f,
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = text, style = AppTypography.smallTitle,
                                color = if (selected) colors.secondary else colors.unfocused
                            )
                        }
                    }
                }
                Item("支出账单", state.value is OutlayType) {
                    state.value = OutlayType.Other
                }
                Item("收入账单", state.value is IncomeType) {
                    state.value = IncomeType.Other
                }
                Spacer(modifier = Modifier.weight(0.4f))
            }
            Column(
                modifier = Modifier
                    .rsBlurShadow(
                        radius = 10.dp,
                        shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp),
                        color = colors.unfocused.copy(alpha = 0.2f),
                        offset = DpOffset(0.dp, 10.dp)
                    )
                    .clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
                    .background(colors.background)
                    .padding(horizontal = Gap.Large, vertical = Gap.Large)
                    .zIndex(1f),
                verticalArrangement = Arrangement.spacedBy(Gap.Big, Alignment.Top),
            ) {


                ItemInput("名称",
                    KeyboardType.Text,
                    name,
                    onValueChange = {
                        if (it.length <= Config.NAME_MAX_LENGTH) {
                            name.value = it
                        }
                    }
                )
                ItemType(
                    "分类",
                    state.value.toStringList,
                    state.value.cnName
                ) {
                    if(state.value is OutlayType){
                        state.value = it.toOutlayType!!
                    }else{
                        state.value = it.toIncomeType!!
                    }
                }
                ItemSelect(
                    "日期",
                    date.value.toString
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
                Spacer(modifier = Modifier.height(Gap.Large))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    FillButton(
                        modifier = Modifier
                            .padding(horizontal = Gap.Mid, vertical = Gap.Small),
                        color = colors.primary,
                        style = AppTypography.smallTitle,
                        textColor = colors.background,
                        text = "确定"
                    ) {
                        if(checkValues(name.value,amount.value, onError = {
                                context.toast(it)
                            })){
                            vm.insertBill(
                                Bill(
                                    name.value,
                                    "",
                                    state.value,
                                    amount.value,
                                    date.value,
                                    ""
                                ),
                                IOCallback(
                                    onSuccess = {
                                        nav.pop()
                                        context.toast("添加成功")

                                    },
                                    onError = {
                                        context.toast("添加失败"+it.message)
                                    }
                                )
                            )
                        }
                    }
                }
            }
        }
    }

}

@Composable
private fun ItemInput(
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
private fun ItemSelect(
    text: String,
    value: String,
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
                .padding(start = Gap.Mid)
        ) {
            PlainTextField(
                value = value,
                hint = "限制长度为${Config.NAME_MAX_LENGTH}",
                enabled = false,
                onValueChange = {},
                singleLine = true,
                maxLines = 1,
                textStyle = AppTypography.inputMsg,
                hintColor = colors.unfocused
            )
            Spacer(modifier = Modifier.weight(1f))
            EasyImage(
                src = R.drawable.select,
                contentDescription = "选择",
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .clip(CardShapes.small)
                    .click {

                    }
            )
        }
    }
}

@Composable
private fun ItemType(
    text: String,
    list: List<String>,
    type: String,
    onClick: (String) -> Unit,
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text,
            style = AppTypography.smallMsg,
            modifier = Modifier.padding(vertical = Gap.Small)
        )
        Spacer(modifier = Modifier.width(Gap.Big))
        SelectType(
            list, listOf(type), onClick
        )
    }
}



private fun checkValues(
    name:String,
    amount:String,
    onError:(String)->Unit
):Boolean{
    if(name.isBlank()){
        onError("名称不可为空")
        return false
    }

    if(amount.isBlank()){
        onError("金额不可为空")
        return false
    }

    if(amount.toBigDecimal()==BigDecimal("0.00")){
        onError("金额不可为0")
        return false
    }

    return true
}