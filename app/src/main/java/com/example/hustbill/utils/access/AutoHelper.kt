package com.example.hustbill.utils.access

import android.view.accessibility.AccessibilityNodeInfo
import com.example.hustbill.config.OutlayType
import com.example.hustbill.db.AutoRecord
import com.example.hustbill.db.AutoRecordHelper
import com.example.hustbill.db.Bill
import com.example.hustbill.db.BillHelper
import com.example.hustbill.db.IOCallback
import com.example.hustbill.utils.getDate

interface AutoHelper {

    suspend fun insertBill(autoRecord: AutoRecord, source:String,onSuccess:()->Unit) {
        BillHelper.insertBill(
            Bill(
                autoRecord.msg,
                "",
                OutlayType.Other,
                autoRecord.amount,
                getDate(),
                source
            ),
            IOCallback(
                onErrorHandler = {
                    AutoRecordHelper.insertAutoRecord(
                        autoRecord,
                        IOCallback(
                            onSuccess = {onSuccess()}
                        )
                    )
                },
                onSuccess = {onSuccess()}
            )
        )
    }

    fun isTargetScreen(className:String,packetName:String):Boolean

    fun checkTarget(root: AccessibilityNodeInfo):Boolean

    fun checkRepeat(content:String,windowId: Int):Boolean

    fun resolveContent(
        packetName: String,
        className: String,
        windowId: Int,
        content: String,
    )

}