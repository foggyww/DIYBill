package com.example.hustbill.ui.screen.setting

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.hustbill.utils.watching.WatchUsageUtil
import com.example.hustbill.utils.watching.WindowServiceHelper
import kotlinx.coroutines.delay

class SettingViewModel:ViewModel() {

    private val winHelper = WindowServiceHelper()

    fun openOver(context:Context){
        winHelper.connectToService(context) {
            while (true) {
                delay(1000)
                val list = WatchUsageUtil.getUsageStats(context)
                if (list.isNotEmpty()) {
                    val s =
                        list.firstOrNull()?.packageName + "\n" + list.firstOrNull()?.className
                    winHelper.updateText(s)
                }
            }
        }
    }

    fun closeOver(context: Context){
        winHelper.disconnectToService(context)
    }
}