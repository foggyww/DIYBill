package com.example.diybill.utils.access

import android.view.accessibility.AccessibilityNodeInfo
import com.example.diybill.config.OutlayType
import com.example.diybill.db.AutoRecord
import com.example.diybill.db.AutoRecordHelper
import com.example.diybill.db.Bill
import com.example.diybill.db.BillHelper
import com.example.diybill.db.IOCallback
import com.example.diybill.utils.getDate
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

abstract class AutoHelper(
    private val packetName: String,
    private val classNames: List<String>,
) {

    protected abstract val startWith:String

    suspend fun insertBill(autoRecord: AutoRecord, source: String, onSuccess: () -> Unit) {
        BillHelper.insertBill(
            Bill(
                -1,
                autoRecord.msg,
                "",
                OutlayType.Other,
                autoRecord.amount,
                getDate(),
                source,
                emptyList()
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

        if (this.classNames.contains(className) && packetName == this.packetName) {
            this.isWatching = true
        } else if(className.startsWith(startWith)){
            if(isWatching){
                this.isWatching = false
                oldContent = ""
            }
        }
    }

    var isWatching: Boolean = false
        private set

    abstract fun checkTarget(root: AccessibilityNodeInfo): Boolean

    private fun checkRepeat(content: String): Boolean {
        return oldContent != content
    }

    protected abstract suspend fun resolveContent(
        packetName: String,
        className: String,
        windowId: Int,
        content: String,
        onSuccess: () -> Unit,
    )

    private val mutex = Mutex()

    @OptIn(DelicateCoroutinesApi::class)
    fun startResolve(
        packetName: String,
        className: String,
        windowId: Int,
        content: String,
        onSuccess: () -> Unit,
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            mutex.lock()
            try {
                if(checkRepeat(content)) {
                    resolveContent(packetName, className, windowId, content) {
                        onSuccess()
                        oldContent = content
                    }
                }
            } catch (t: Throwable) {
                t.printStackTrace()
            } finally {
                mutex.unlock()
            }
        }
    }

}