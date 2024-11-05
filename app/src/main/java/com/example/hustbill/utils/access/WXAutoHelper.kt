package com.example.hustbill.utils.access

import android.view.accessibility.AccessibilityNodeInfo
import com.example.hustbill.db.AutoRecord
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WXAutoHelper:AutoHelper {

    companion object{
        private const val PACKET_NAME = "com.tencent.mm"
        private const val CLASS_NAME = "com.tencent.mm.framework.app.UIPageFragmentActivity"
    }

    override fun isTargetScreen(className: String, packetName: String):Boolean {
        return className == CLASS_NAME && packetName== PACKET_NAME
    }

    override fun checkTarget(root: AccessibilityNodeInfo):Boolean {
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
        } else if(description=="支付成功"){
           return true
        }  else{
            for (i in 0..<root.childCount) {
                if (checkTarget(root.getChild(i))) {
                    return true
                }
            }
            return false
        }
    }

    private var oldContent:String = ""
    private var oldWindowId:Int = -1
    override fun checkRepeat(content: String, windowId: Int): Boolean {
        return content!=oldContent || oldWindowId!=windowId
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun resolveContent(
        packetName: String,
        className: String,
        windowId: Int,
        content: String
    ) {
        val list = content.split("\n")
        for (i in list.indices) {
            try {
                if (list[i].contains('￥')) {
                    val msg = list[i - 1]
                    Regex("\\d+\\.\\d+").find(list[i])?.value?.let { amount ->
                        GlobalScope.launch(Dispatchers.IO){
                            insertBill(
                                AutoRecord(msg, amount, packetName, className, windowId),
                                "微信",
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