package com.example.hustbill.utils.access

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.example.hustbill.db.AutoRecord
import com.example.hustbill.db.AutoRecordHelper
import com.example.hustbill.db.IOCallback
import com.example.hustbill.ui.screen.index.stringList
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
class MyAccessibilityService : AccessibilityService() {

    private fun getTextFromNode(nodeInfo: AccessibilityNodeInfo?): String {
        nodeInfo?.let {
            if (nodeInfo.childCount == 0) {
                if (it.text != null) {
                    return it.text.toString()
                } else {
                    return ""
                }
            } else {
                val builder = StringBuilder()
                for (i in 0..<nodeInfo.childCount) {
                    val cT = getTextFromNode(it.getChild(i))
                    if (cT.isNotEmpty()) {
                        builder.append(cT).append("\n")
                    }
                }
                return builder.toString()
            }
        } ?: run {
            return ""
        }
    }

    private var oldString = ""

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.apply {
            stringList.add(windowId.toString())
            stringList.add(packageName.toString())
            stringList.add(className.toString())
            //支持微信的
            if (className.toString() == "com.tencent.mm.framework.app.UIPageFragmentActivity" && packageName.toString() == "com.tencent.mm") {
                source?.let {
                    if (checkWX(it)) {
                        val content = getTextFromNode(it)
                        if (content != oldString) {
                            oldString = content
                        } else {
                            return
                        }
                        try {
                            resolveContent(
                                packageName.toString(),
                                className?.toString() ?: "null",
                                windowId,
                                getTextFromNode(it)
                            )
                        }catch (_:Throwable){}
                    }
                }
            }
        }
    }

    private fun checkWX(root: AccessibilityNodeInfo): Boolean {
        if (root.className == "android.widget.Button" &&
            root.viewIdResourceName == "com.tencent.mm:id/kinda_button_impl_wrapper"
        ) {
            return true
        } else {
            for (i in 0..<root.childCount) {
                if (checkWX(root.getChild(i))) {
                    return true
                }
            }
            return false
        }
    }

    private fun resolveContent(
        packetName: String,
        className: String,
        windowId: Int,
        string: String,
    ) {
        val list = string.split("\n")
        for (i in list.indices) {
            try {
                if (list[i].contains('￥')) {
                    val msg = list[i - 1]
                    Regex("\\d+\\.\\d+").find(list[i])?.value?.let { amount ->
                        GlobalScope.launch(Dispatchers.IO){
                            AutoRecordHelper.insertAutoRecord(
                                AutoRecord(msg, amount, packetName, className, windowId),
                                IOCallback()
                            )
                        }
                    }
                }
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }

    override fun onInterrupt() {

    }

}