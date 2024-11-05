package com.example.hustbill.utils.access

import android.view.accessibility.AccessibilityNodeInfo
import com.example.hustbill.db.AutoRecord
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ALIAutoHelper:AutoHelper {
    companion object{
        private const val PACKET_NAME = "com.eg.android.AlipayGphone"
        private const val CLASS_NAME = "com.alipay.android.msp.ui.views.MspContainerActivity"
    }

    override fun isTargetScreen(className: String, packetName: String): Boolean {
        return className == CLASS_NAME && packetName== PACKET_NAME
    }

    override fun checkTarget(root: AccessibilityNodeInfo): Boolean {
        return true
    }

    private var oldContent:String = ""
    private var oldWindowId:Int = -1

    override fun checkRepeat(content: String, windowId: Int):Boolean {
        return content!=oldContent || oldWindowId!=windowId
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun resolveContent(
        packetName: String,
        className: String,
        windowId: Int,
        content: String,
    ) {
        val list = content.split("\n")
        for (i in list.indices) {
            try {
                if (list[i].contains('￥')) {
                    val index = list[i].indexOf('￥')
                    val msg = if (index != -1) {
                        list[i].substring(0, index)
                    } else {
                        "未找到 '￥' 符号"
                    }
                    Regex("\\d+\\.\\d+").find(list[i])?.value?.let { amount ->
                        GlobalScope.launch(Dispatchers.IO){
                            insertBill(
                                AutoRecord(msg, amount, packetName, className, windowId),
                                "支付宝",
                                onSuccess = {
                                    oldContent = content
                                    oldWindowId = windowId
                                }
                            )
                        }
                    }
                }
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }
}