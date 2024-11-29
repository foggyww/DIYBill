package com.example.diybill.utils.access

import android.view.accessibility.AccessibilityNodeInfo
import com.example.diybill.db.AutoRecord
import kotlinx.coroutines.DelicateCoroutinesApi

class ALIAutoHelper : AutoHelper(PACKET_NAME, listOf(CLASS_NAME1)) {
    companion object {
        private const val PACKET_NAME = "com.eg.android.AlipayGphone"
        private const val CLASS_NAME1 = "com.alipay.android.msp.ui.views.MspContainerActivity"
    }

    override val startWith: String
        get() = "com.alipay"

    override fun checkTarget(root: AccessibilityNodeInfo): Boolean {
        return checkText(root)==3 //&& checkButton(root)
    }

    private fun checkText(root: AccessibilityNodeInfo): Int {
        val className = root.className
        val viewIdResourceName = root.viewIdResourceName
        val description = root.contentDescription
        val text = root.text
        println(className)
        println(viewIdResourceName)
        println(description)
        println(text)
        var sum = 0
        if (className == "android.widget.TextView" && viewIdResourceName == "com.alipay.android.app:id/nav_right_textview"
            &&text=="完成") {
            sum+=1
        }
        if(className=="android.widget.TextView"&&text=="付款方式"){
            sum+=1
        }
        if(className== "android.widget.TextView"&&text=="收款方"){
            sum+=1
        }
        if(className=="android.widget.TextView"&&text=="订单金额"){
            sum+=1
        }
        for (i in 0..<root.childCount) {
            try {
                sum += checkText(root.getChild(i))
            } catch (_: Throwable) {}
        }
        return sum

    }

//    private fun checkButton(root: AccessibilityNodeInfo): Boolean {
//        val className = root.className
//        val viewIdResourceName = root.viewIdResourceName
//        val description = root.contentDescription
//        println(className)
//        println(viewIdResourceName)
//        println(description)
//        if (className == "android.widget.Button" &&
//            viewIdResourceName == "com.tencent.mm:id/kinda_button_impl_wrapper"
//        ) {
//            return true
//        } else {
//            for (i in 0..<root.childCount) {
//                try {
//                    if (checkButton(root.getChild(i))) {
//                        return true
//                    }
//                } catch (t: Throwable) {
//                    return false
//                }
//            }
//            return false
//        }
//    }

    override suspend fun resolveContent(
        packetName: String,
        className: String,
        windowId: Int,
        content: String,
    ):AutoRecord {
        val list = content.split("\n")
        for (i in list.indices) {
            if (list[i].contains('￥')) {
                val index = list[i].indexOf('￥')
                val msg = if (index != -1) {
                    list[i].substring(0, index)
                } else {
                    "未找到 '￥' 符号"
                }
                if(msg.isNotEmpty()){
                    Regex("\\d+\\.\\d+").find(list[i])?.value?.let { amount ->
                        return AutoRecord(msg, amount, packetName, className, windowId,"支付宝")
                    }
                }
            }
        }
        throw Throwable("未找到对应的信息")
    }
}