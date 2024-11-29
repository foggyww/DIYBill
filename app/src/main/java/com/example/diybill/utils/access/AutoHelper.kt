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

    private var oldContent = ""
    /**
     * @param autoRecord:由子类处理后的自动记账的记录信息
     * @param onSuccess:执行成功后
     * 已实现，子类通过调用该函数来向数据库中插入账单
     */
    private suspend fun insertBill(autoRecord: AutoRecord, onSuccess: () -> Unit) {
        BillHelper.insertBill(
            Bill(
                -1,
                autoRecord.msg,
                "",
                OutlayType.Other,
                autoRecord.amount,
                getDate(),
                autoRecord.source,
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

    /**
     * @param className:类名
     * @param packetName:包名
     * 已实现，通过调用该函数来尝试开启监视，若跳转到了同一包名的非监视类，则关闭监视，清除oldContent
     */
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

    /**
     * @param root:当前屏幕根节点
     * 通过当前屏幕根节点来遍历检测是否为目标屏幕
     */
    abstract fun checkTarget(root: AccessibilityNodeInfo): Boolean

    private fun checkRepeat(content: String): Boolean {
        return oldContent != content
    }

    /**
     * @param packetName:包名
     * @param className:类名
     * @param windowId:当前窗口id
     * @param content:所需处理的文字内容
     * @return 返回一个自动记录类
     * 用来处理文屏幕字信息
     */
    protected abstract suspend fun resolveContent(
        packetName: String,
        className: String,
        windowId: Int,
        content: String,
    ):AutoRecord

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
                    val auto = resolveContent(packetName, className, windowId, content)
                    insertBill(auto,onSuccess)
                    oldContent = content
                }
            } catch (t: Throwable) {
                t.printStackTrace()
            } finally {
                mutex.unlock()
            }
        }
    }

}