package com.example.diybill.utils.access

import android.view.accessibility.AccessibilityNodeInfo
import com.example.diybill.db.AutoRecord

class WXAutoHelper : AutoHelper(PACKET_NAME, listOf(CLASS_NAME1, CLASS_NAME2)) {

    companion object {
        private const val PACKET_NAME = "com.tencent.mm"
        private const val CLASS_NAME1 = "com.tencent.mm.framework.app.UIPageFragmentActivity"
        private const val CLASS_NAME2 = "com.tencent.mm.plugin.scanner.ui.BaseScanUI"
    }

    override val startWith: String
        get() = PACKET_NAME

    override fun checkTarget(root: AccessibilityNodeInfo): Boolean {
        return checkText(root) && checkButton(root)
    }

    private fun checkText(root: AccessibilityNodeInfo): Boolean {
        val className = root.className
        val viewIdResourceName = root.viewIdResourceName
        val description = root.contentDescription
        println(className)
        println(viewIdResourceName)
        println(description)
        if (className == "android.view.ViewGroup" && description == "支付成功") {
            return true
        } else {
            for (i in 0..<root.childCount) {
                try {
                    if (checkText(root.getChild(i))) {
                        return true
                    }
                } catch (t: Throwable) {
                    return false
                }
            }
            return false
        }
    }

    private fun checkButton(root: AccessibilityNodeInfo): Boolean {
        val className = root.className
        val viewIdResourceName = root.viewIdResourceName
        val description = root.contentDescription
        println(className)
        println(viewIdResourceName)
        println(description)
        if (className == "android.widget.Button" &&
            viewIdResourceName == "com.tencent.mm:id/kinda_button_impl_wrapper"
        ) {
            return true
        } else {
            for (i in 0..<root.childCount) {
                try {
                    if (checkButton(root.getChild(i))) {
                        return true
                    }
                } catch (t: Throwable) {
                    return false
                }
            }
            return false
        }
    }

    override suspend fun resolveContent(
        packetName: String,
        className: String,
        windowId: Int,
        content: String,
    ):AutoRecord {
        val list = content.split("\n")
        for (i in list.indices) {
            if (list[i].contains('￥')) {
                val msg = list[i - 1]
                Regex("\\d+\\.\\d+").find(list[i])?.value?.let { amount ->
                    return AutoRecord(msg, amount, packetName, className, windowId,"微信支付")
                }
            }
        }
        throw Throwable("未找到自动记账内容")
    }

}