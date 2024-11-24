package com.example.diybill.utils.access

import android.view.accessibility.AccessibilityNodeInfo
import com.example.diybill.db.AutoRecord
import kotlinx.coroutines.DelicateCoroutinesApi

class ALIAutoHelper : AutoHelper(PACKET_NAME, listOf(CLASS_NAME1)) {
    companion object {
        private const val PACKET_NAME = "com.eg.android.AlipayGphone"
        private const val CLASS_NAME1 = "com.alipay.android.msp.ui.views.MspContainerActivity"
    }


    override fun checkTarget(root: AccessibilityNodeInfo): Boolean {
        return false
    }

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun resolveContent(
        packetName: String,
        className: String,
        windowId: Int,
        content: String,
        onSuccess: () -> Unit,
    ) {
        val list = content.split("\n")
        for (i in list.indices) {
            if (list[i].contains('￥')) {
                val index = list[i].indexOf('￥')
                val msg = if (index != -1) {
                    list[i].substring(0, index)
                } else {
                    "未找到 '￥' 符号"
                }
                Regex("\\d+\\.\\d+").find(list[i])?.value?.let { amount ->
                    insertBill(
                        AutoRecord(msg, amount, packetName, className, windowId),
                        "支付宝",
                        onSuccess = {
                            onSuccess()
                        }
                    )
                }
            }
        }
    }
}