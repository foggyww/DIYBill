package com.example.diybill.ui.screen.support

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import com.example.diybill.R

data class SupportApp(
    val name:String,
    @DrawableRes val icon:Int
)

val supportList = listOf(
    SupportApp("微信支付", R.drawable.wexin),
    SupportApp("支付宝",R.drawable.zfb)
)

class SupportViewModel:ViewModel() {

}