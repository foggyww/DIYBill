package com.example.diybill.ui.screen.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.diybill.R
import com.example.diybill.config.Config
import com.example.diybill.config.IncomeType
import com.example.diybill.config.OutlayType
import com.example.diybill.config.Type
import com.example.diybill.config.toTypeList
import com.example.diybill.db.IOCallback
import com.example.diybill.pop
import com.example.diybill.ui.provider.ChooseFile
import com.example.diybill.ui.provider.LocalNav
import com.example.diybill.ui.provider.LocalPicker
import com.example.diybill.ui.provider.toast
import com.example.diybill.ui.theme.AppTypography
import com.example.diybill.ui.theme.CardShapes
import com.example.diybill.ui.theme.Gap
import com.example.diybill.ui.theme.ImageSize
import com.example.diybill.ui.theme.RoundedShapes
import com.example.diybill.ui.theme.colors
import com.example.diybill.ui.widgets.AppDialog
import com.example.diybill.ui.widgets.EasyImage
import com.example.diybill.ui.widgets.FillButton
import com.example.diybill.ui.widgets.InputCard
import com.example.diybill.ui.widgets.MemCacheImage
import com.example.diybill.ui.widgets.PlainTextField
import com.example.diybill.ui.widgets.SelectType
import com.example.diybill.ui.widgets.ShadowLayout
import com.example.diybill.ui.widgets.TextHeader
import com.example.diybill.ui.widgets.TitleSpacer
import com.example.diybill.ui.widgets.rememberDateDialog
import com.example.diybill.utils.Date
import com.example.diybill.utils.click
import com.example.diybill.utils.clickNoRepeat
import com.example.diybill.utils.getDate
import com.example.diybill.utils.string
import com.example.diybill.utils.toString
import com.gigamole.composeshadowsplus.rsblur.rsBlurShadow
import java.math.BigDecimal

@Composable
fun AddScreen(
    contentPadding: PaddingValues,
    vm: AddViewModel = viewModel(),
) {

    val name = remember {
        mutableStateOf("")
    }
    val amount = remember {
        mutableStateOf("")
    }
    val state: MutableState<Type> = remember {
        mutableStateOf(OutlayType.Other)
    }
    val date = remember {
        mutableStateOf(getDate())
    }
    val context = LocalContext.current
    val showDateDialog = remember { mutableStateOf(false) }
    val nav = LocalNav.current
    val picker = LocalPicker.current
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colors.background
    ) {
        DateDialog(
            show = showDateDialog,
            onChangeDate = {
                date.value = it
            },
            initDate = getDate()
        ) {
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
                                modifier = Modifier
                                    .fillMaxSize(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
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
                        hint = "限制长度为${Config.NAME_MAX_LENGTH}",
                        name.value,
                        onValueChange = {
                            if (it.length <= Config.NAME_MAX_LENGTH) {
                                name.value = it
                            }
                        },
                    )
                    ItemType(
                        "分类",
                        state.value.toTypeList,
                        state.value
                    ) {
                        if (state.value is OutlayType) {
                            state.value = it
                        } else {
                            state.value = it
                        }
                    }
                    ItemSelect(
                        "日期",
                        date.value.toString,
                    ){
                        showDateDialog.value = true
                    }
                    ItemInput("金额",
                        KeyboardType.Number,
                        hint = "限制金额为${Config.AMOUNT_MAX_LENGTH}",
                        amount.value,
                        onValueChange = {
                            try {
                                val bd = BigDecimal(it).setScale(2)
                                if (bd <= BigDecimal(Config.AMOUNT_MAX_LENGTH)
                                    && bd >= BigDecimal(Config.AMOUNT_MIN_LENGTH)
                                ) {
                                    amount.value = it
                                }
                            } catch (_: Throwable) {
                                if (it == "") {
                                    amount.value = it
                                }
                            }
                        })
                    ItemImage(vm.imageList.toList(),
                        onAdd = {
                            picker.launch(ChooseFile {
                                vm.imageList.add(it)
                            })
                        },
                        onClose = {
                            vm.imageList.removeAt(it)
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
                            if (checkValues(name.value, amount.value, onError = {
                                    context.toast(it)
                                })) {
                                vm.insertBill(
                                    name.value,
                                    state.value,
                                    BigDecimal(amount.value).setScale(2).toString(),
                                    date.value,
                                    vm.imageList.toList(),
                                    IOCallback(
                                        onSuccess = {
                                            nav.pop()
                                            context.toast("添加成功")

                                        },
                                        onError = {
                                            context.toast("添加失败" + it.message)
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

}


@Composable
fun ItemInput(
    text: String,
    type: KeyboardType,
    hint:String,
    value: String,
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
                .padding(horizontal = Gap.Mid)
        ) {
            PlainTextField(
                modifier = Modifier.padding(vertical = Gap.Small)
                    .fillMaxWidth(),
                value = value,
                hint = hint,
                onValueChange = onValueChange,
                keyboardOptions = KeyboardOptions(
                    keyboardType = type
                ),
                textStyle = AppTypography.inputMsg,
                hintColor = colors.unfocused
            )
        }
    }
}

@Composable
fun DateDialog(
    show:MutableState<Boolean>,
    onChangeDate:(Date)->Unit,
    initDate: Date,
   content: @Composable ()->Unit
){

    val dateDialog = rememberDateDialog(
        initDate = initDate,
        positive = {
            onChangeDate.invoke(it)
            show.value = false
        },
        negative = {
            show.value = false
        }
    )
    dateDialog.Build(
        show = show.value,
        properties = DialogProperties(),
        onDismissRequest = {
            show.value = false
        }
    ) {
        content()
    }


}

@Composable
fun ItemSelect(
    text: String,
    value: String,
    onClick:()->Unit
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
                modifier = Modifier,
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
                        onClick.invoke()
                    }
            )
        }
    }
}

@Composable
fun ItemImage(
    imgList: List<String>,
    onAdd: () -> Unit,
    onClose: (Int) -> Unit,
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Gap.Big),
        horizontalArrangement = Arrangement.spacedBy(Gap.Mid, Alignment.Start)
    ) {
        var first = true
        for (i in 0..<Config.MAX_IMAGE_COUNT) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(colors.label, CardShapes.extraSmall)
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                imgList.getOrNull(i)?.let {
                    Box(modifier = Modifier.clip(CardShapes.extraSmall)) {
                        MemCacheImage(
                            modifier = Modifier.fillMaxSize(),
                            path = it
                        )
                    }
                    EasyImage(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(ImageSize.Small / 2, -(ImageSize.Small / 2))
                            .size(ImageSize.Small * 1.2f)
                            .clip(RoundedShapes.large)
                            .background(colors.background)
                            .clickNoRepeat {
                                onClose(i)
                            },
                        src = R.drawable.close,
                        contentDescription = "移除图片"
                    )
                } ?: run {
                    if (first) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickNoRepeat {
                                    onAdd.invoke()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            EasyImage(
                                src = R.drawable.add_image,
                                contentDescription = "添加图片",
                                modifier = Modifier.fillMaxSize(0.5f),
                                tint = colors.unfocused
                            )
                        }
                        first = false
                    }
                }
            }
        }
    }
}

@Composable
fun ItemType(
    text: String,
    list: List<Type>,
    type: Type,
    onClick: (Type) -> Unit,
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
            modifier = Modifier.fillMaxWidth(),
            list, listOf(type), colors.secondary, onClick
        )
    }
}


fun checkValues(
    name: String,
    amount: String,
    onError: (String) -> Unit,
): Boolean {
    if (name.isBlank()) {
        onError("名称不可为空")
        return false
    }

    if (amount.isBlank()) {
        onError("金额不可为空")
        return false
    }

    if (amount.toBigDecimal() == BigDecimal("0.00")) {
        onError("金额不可为0")
        return false
    }

    return true
}