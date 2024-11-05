package com.example.hustbill.utils.access

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.example.hustbill.config.OutlayType
import com.example.hustbill.db.AutoRecord
import com.example.hustbill.db.AutoRecordHelper
import com.example.hustbill.db.Bill
import com.example.hustbill.db.BillHelper
import com.example.hustbill.db.IOCallback
import com.example.hustbill.ui.screen.setting.stringList
import com.example.hustbill.utils.getDate
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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

    private val autoHelpers = listOf(WXAutoHelper(),ALIAutoHelper())

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.apply {
            source?.let { root->
                autoHelpers.forEach {
                    val packetName = packageName.toString()
                    val className = className.toString()
                    if(it.isTargetScreen(className,packetName)
                         && it.checkTarget(root)){
                        val content = getTextFromNode(root)
                        if(it.checkRepeat(content,windowId)){
                            it.resolveContent(packetName,className,windowId,content)
                        }
                    }
                }
            }
        }
    }

    override fun onInterrupt() {

    }

}