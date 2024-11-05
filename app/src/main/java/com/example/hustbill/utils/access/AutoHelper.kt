package com.example.hustbill.utils.access

import android.view.accessibility.AccessibilityNodeInfo
import com.example.hustbill.config.OutlayType
import com.example.hustbill.db.AutoRecord
import com.example.hustbill.db.AutoRecordHelper
import com.example.hustbill.db.Bill
import com.example.hustbill.db.BillHelper
import com.example.hustbill.db.IOCallback
import com.example.hustbill.utils.getDate
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

abstract class AutoHelper(
    private val packetName: String,
    private val className: String,
) {

    private val mutex = Mutex()
    suspend fun insertBill(autoRecord: AutoRecord, source: String, onSuccess: () -> Unit) {
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
                                onSuccess = { onSuccess() }
                            )
                        )
                    },
                    onSuccess = { onSuccess() }
                )
            )
    }

    private var oldContent = ""
    fun tryStartWatch(className: String, packetName: String) {
        if (this.isWatching) {
            //离开监视
            if (className.startsWith("com.tencent.mm")) {
                this.isWatching = false
                oldContent = ""
            }
        } else {
            if (className == this.className && packetName == this.packetName) {
                this.isWatching = true
            }
        }
    }

    var isWatching: Boolean = false
        private set

    abstract fun checkTarget(root: AccessibilityNodeInfo): Boolean

    fun checkRepeat(content: String): Boolean {
        return oldContent != content
    }

    protected abstract suspend fun resolveContent(
        packetName: String,
        className: String,
        windowId: Int,
        content: String,
        onSuccess: () -> Unit,
    )

    private var isRunning = false
    @OptIn(DelicateCoroutinesApi::class)
    fun startResolve(
        packetName: String,
        className: String,
        windowId: Int,
        content: String,
        onSuccess: () -> Unit,
    ) {
        if(!isRunning){
            isRunning = true
            GlobalScope.launch(Dispatchers.IO){
                try {
                    resolveContent(packetName, className, windowId, content) {
                        onSuccess()
                        oldContent = content
                    }
                }catch (t:Throwable){
                    t.printStackTrace()
                }finally {
                    isRunning = false
                }
            }
        }
    }

}