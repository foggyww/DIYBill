package com.example.diybill.ui.screen.update

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
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
import com.example.diybill.R
import com.example.diybill.config.Config
import com.example.diybill.config.OutlayType
import com.example.diybill.config.Type
import com.example.diybill.config.toTypeList
import com.example.diybill.db.IOCallback
import com.example.diybill.pop
import com.example.diybill.ui.provider.ChooseFile
import com.example.diybill.ui.provider.LocalNav
import com.example.diybill.ui.provider.LocalPicker
import com.example.diybill.ui.provider.toast
import com.example.diybill.ui.screen.add.DateDialog
import com.example.diybill.ui.screen.add.ItemInput
import com.example.diybill.ui.screen.add.ItemSelect
import com.example.diybill.ui.screen.add.ItemType
import com.example.diybill.ui.screen.add.checkValues
import com.example.diybill.ui.theme.AppTypography
import com.example.diybill.ui.theme.CardShapes
import com.example.diybill.ui.theme.Gap
import com.example.diybill.ui.theme.ImageSize
import com.example.diybill.ui.theme.RoundedShapes
import com.example.diybill.ui.theme.colors
import com.example.diybill.ui.theme.red2
import com.example.diybill.ui.widgets.EasyImage
import com.example.diybill.ui.widgets.FillButton
import com.example.diybill.ui.widgets.MemCacheImage
import com.example.diybill.ui.widgets.TextHeader
import com.example.diybill.ui.widgets.TitleSpacer
import com.example.diybill.utils.clickNoRepeat
import com.example.diybill.utils.getDate
import com.example.diybill.utils.toString
import com.gigamole.composeshadowsplus.rsblur.rsBlurShadow
import java.math.BigDecimal

@Composable
fun UpdateScreen(
    billId: Int,
    contentPadding: PaddingValues,
    vm: UpdateViewModel = viewModel(),
) {
    vm.init(billId)
    val bill by vm.bill
    val name = remember(bill) {
        mutableStateOf(bill?.name)
    }
    val amount = remember(bill) {
        mutableStateOf(bill?.amount)
    }
    val state: MutableState<Type?> = remember(bill) {
        mutableStateOf(bill?.type)
    }
    val date = remember(bill) {
        mutableStateOf(bill?.date)
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
            initDate = date.value?: getDate()
        ){
            bill?.let {
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
                            name.value ?: "",
                            onValueChange = {
                                if (it.length <= Config.NAME_MAX_LENGTH) {
                                    name.value = it
                                }
                            }
                        )
                        state.value?.let {
                            ItemType(
                                "分类",
                                it.toTypeList,
                                it
                            ) {
                                if (state.value is OutlayType) {
                                    state.value = it
                                } else {
                                    state.value = it
                                }
                            }
                        }
                        date.value?.let {
                            ItemSelect(
                                "日期",
                                it.toString
                            ){
                                showDateDialog.value = true
                            }
                        }
                        ItemInput("金额",
                            KeyboardType.Number,
                            hint = "限制金额为${Config.AMOUNT_MAX_LENGTH}",
                            amount.value ?: "",
                            onValueChange = {
                                try {
                                    val bd = BigDecimal(it).setScale(2)
                                    if (bd <= BigDecimal("10000.00")
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
                        ItemImage(vm.imageList.toList(),
                            onAdd = {
                                picker.launch(ChooseFile {
                                    vm.imageList.add(Pair(false, it))
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
                                color = red2,
                                style = AppTypography.smallTitle,
                                textColor = colors.background,
                                text = "删除"
                            ) {
                                bill?.let {
                                    vm.deleteBill(it,
                                        IOCallback(
                                            onSuccess = {
                                                nav.pop()
                                                context.toast("删除成功")
                                            },
                                            onError = {
                                                context.toast("删除失败")
                                            }
                                        ))
                                }
                            }
                            Spacer(modifier = Modifier.width(Gap.Large))
                            FillButton(
                                modifier = Modifier
                                    .padding(horizontal = Gap.Mid, vertical = Gap.Small),
                                color = colors.primary,
                                style = AppTypography.smallTitle,
                                textColor = colors.background,
                                text = "确定"
                            ) {
                                if (checkValues(name.value!!, amount.value!!, onError = {
                                        context.toast(it)
                                    })) {
                                    vm.updateBill(
                                        name.value!!,
                                        state.value!!,
                                        BigDecimal(amount.value).setScale(2).toString(),
                                        date.value!!,
                                        vm.imageList.toList(),
                                        IOCallback(
                                            onSuccess = {
                                                nav.pop()
                                                context.toast("更改成功")

                                            },
                                            onError = {
                                                context.toast("更改失败" + it.message)
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

}

@Composable
private fun ItemImage(
    imgList: List<Pair<Boolean, String>>,
    onAdd: () -> Unit,
    onClose: (Int) -> Unit,
) {
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
                            path = it.second
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