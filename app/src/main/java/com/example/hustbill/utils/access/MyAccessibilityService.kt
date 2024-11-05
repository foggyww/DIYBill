package com.example.hustbill.utils.access

import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.WindowManager.LayoutParams
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.example.hustbill.config.Config
import com.example.hustbill.databinding.LayoutFloatingImageBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

class MyAccessibilityService : AccessibilityService() {

    private fun getTextFromNode(nodeInfo: AccessibilityNodeInfo?): String {
        nodeInfo?.let {
            if (nodeInfo.childCount == 0) {
                val res = StringBuilder()

                if (it.text != null) {
                    res.append(it.text.toString()).append("\n")
                }
                if (it.contentDescription != null) {
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

    private val autoHelpers = listOf(WXAutoHelper(), ALIAutoHelper())

    @OptIn(DelicateCoroutinesApi::class)
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        if (!Config.openAuto) return
        event?.apply {
            source?.let { r ->
                val root = rootInActiveWindow
                autoHelpers.forEach {
                    val packetName = packageName.toString()
                    val className = className.toString()
                    if (className.startsWith("com.tencent.mm")) {
                        showFloatingWindow(className)
                    }
                    //尝试开启监视
                    it.tryStartWatch(className, packetName)
                    //若成功开启监视且检测成功
                    if (it.isWatching && it.checkTarget(root)) {
                        val content = getTextFromNode(root)
                        //去重
                        if (it.checkRepeat(content)) {
                            it.startResolve(packetName, className, windowId, content) {
                                showFloatingWindow("记账完成")
                                GlobalScope.launch(Dispatchers.IO) {
                                    delay(5000)
                                    missFloatingWindow()
                                }
                            }
                        }
                    }

                }
            }
        }
    }

    private var viewBinding: LayoutFloatingImageBinding? = null
    private val layoutParams = LayoutParams()

    private fun showFloatingWindow(text: String) {
        viewBinding?.let {
            it.root.alpha = 1.0f
            it.textView.text = text
        }
    }

    private fun missFloatingWindow() {
        viewBinding?.let {
            it.root.alpha = 0.0f
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        removeFloatingWindow()
    }

    @SuppressLint("ClickableViewAccessibility", "InflateParams")
    private fun createFloatingWindow() {
        viewBinding = LayoutFloatingImageBinding.inflate(LayoutInflater.from(this))
        //获取WindowManager服务
        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        //设置LayoutParam
        layoutParams.type = LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
        layoutParams.format = PixelFormat.RGBA_8888
        layoutParams.gravity = Gravity.START or Gravity.TOP
        layoutParams.flags =
            LayoutParams.FLAG_NOT_TOUCH_MODAL or LayoutParams.FLAG_NOT_FOCUSABLE
        layoutParams.width = LayoutParams.WRAP_CONTENT
        layoutParams.height = LayoutParams.WRAP_CONTENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            layoutParams.x = windowManager.currentWindowMetrics.bounds.width()
            layoutParams.y = windowManager.currentWindowMetrics.bounds.width() / 2
        } else {
            layoutParams.x = windowManager.defaultDisplay.width
            layoutParams.y = windowManager.defaultDisplay.height / 2
        }
        viewBinding?.root?.alpha = 0f
        //将悬浮窗控件添加到WindowManager
        windowManager.addView(viewBinding?.root, layoutParams)
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        createFloatingWindow()
    }

    private fun removeFloatingWindow() {
        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        windowManager.removeView(viewBinding?.root)
    }

    override fun onInterrupt() {

    }

}