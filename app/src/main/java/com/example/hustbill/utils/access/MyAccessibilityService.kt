package com.example.hustbill.utils.access

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.example.hustbill.db.AutoRecord
import com.example.hustbill.db.AutoRecordHelper
import com.example.hustbill.db.IOCallback
import com.example.hustbill.ui.screen.setting.stringList
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
class MyAccessibilityService : AccessibilityService() {

    private fun getTextFromNode(nodeInfo: AccessibilityNodeInfo?): String {
        nodeInfo?.let {
            if (nodeInfo.childCount == 0) {
                val res = StringBuilder()

                if (it.text != null) {
                    res.append(it.text.toString()).append("\n")
                }
                if(it.contentDescription!=null){
                    res.append(it.contentDescription.toString()).append("\n")
                }
                return res.toString()
            } else {
                val builder = StringBuilder()
                for (i in 0..<nodeInfo.childCount) {
                    val cT = getTextFromNode(it.getChild(i))
                    if (cT.isNotEmpty()) {
                        builder.append(cT)
                    }
                }
                return builder.toString()
            }
        } ?: run {
            return ""
        }
    }

    private val oldWindowIdList = mutableListOf(-1,-1)
    private val oldStringList = mutableListOf("","")

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.apply {
            //支持微信的
            if (className.toString() == "com.tencent.mm.framework.app.UIPageFragmentActivity" && packageName.toString() == "com.tencent.mm") {
                source?.let {
                    val text = getTextFromNode(it)
                    if (checkWX(it)) {
                        if(oldStringList[0]==text&&windowId==oldWindowIdList[0]){
                            return
                        }
                        try {
                            resolveWeiXinContent(
                                packageName.toString(),
                                className?.toString() ?: "null",
                                windowId,
                                text
                            )
                            oldWindowIdList[0] = windowId
                            oldStringList[0] = text
                        }catch (_:Throwable){}
                    }
                }
            }
            if(className.toString()=="com.alipay.android.msp.ui.views.MspContainerActivity"&& packageName.toString() =="com.eg.android.AlipayGphone"){
                event.source?.let {
                    if (checkAL(it)) {
                        val text = getTextFromNode(it)
                        if (text==oldStringList[1]&&windowId==oldWindowIdList[1]) {
                            return
                        }
                        try {
                            resolveAlipayContent(
                                packageName.toString(),
                                className?.toString() ?: "null",
                                windowId,
                                text
                            )
                            oldWindowIdList[1] = windowId
                            oldStringList[1] = text
                        }catch (_:Throwable){}
                    }
                }
            }
        }
    }

    private fun checkWX(root: AccessibilityNodeInfo): Boolean {
        return true
//        if (root.className == "android.widget.Button" &&
//            root.viewIdResourceName == "com.tencent.mm:id/kinda_button_impl_wrapper"
//        ) {
//            return true
//        } else {
//            for (i in 0..<root.childCount) {
//                if (checkWX(root.getChild(i))) {
//                    return true
//                }
//            }
//            return false
//        }
    }

    private fun checkAL(root: AccessibilityNodeInfo): Boolean {
        if (root.className == "android.widget.Button" &&
            root.viewIdResourceName == "com.tencent.mm:id/kinda_button_impl_wrapper"
        ) {
            return true
        } else {
            for (i in 0..<root.childCount) {
                if (checkAL(root.getChild(i))) {
                    return true
                }
            }
        }
        return true
    }


    private fun resolveWeiXinContent(
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

    private fun resolveAlipayContent(
        packetName: String,
        className: String,
        windowId: Int,
        string: String,
    ) {
        val list = string.split("\n")
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