package com.example.diybill.utils.watching

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.WindowManager.LayoutParams
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.example.diybill.databinding.LayoutFloatingImageBinding
import kotlinx.coroutines.launch

class WindowService: LifecycleService(){

    private val watchingBinder = WatchingBinder()

    inner class WatchingBinder: Binder(){
        fun runOnService(onSuccess:suspend (Context)->Unit){
            lifecycleScope.launch {
                onSuccess.invoke(this@WindowService)
            }
        }
        fun updateText(text:String){
            viewBinding?.let {
                it.textView.text = text
            }
        }
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        showFloatingWindow()
        return watchingBinder
    }

    // 获取悬浮窗布局
    private var viewBinding: LayoutFloatingImageBinding? = null
    private val layoutParams = LayoutParams()

    @SuppressLint("ClickableViewAccessibility", "InflateParams")
    private fun showFloatingWindow() {
        viewBinding = LayoutFloatingImageBinding.inflate(LayoutInflater.from(this))
        //获取WindowManager服务
        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        //设置LayoutParam
        layoutParams.type = LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
        layoutParams.format = PixelFormat.RGBA_8888
        layoutParams.gravity = Gravity.START or Gravity.TOP
        layoutParams.flags =
            LayoutParams.FLAG_NOT_TOUCHABLE or LayoutParams.FLAG_NOT_FOCUSABLE
        layoutParams.width = LayoutParams.WRAP_CONTENT
        layoutParams.height = LayoutParams.WRAP_CONTENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            layoutParams.x =windowManager.currentWindowMetrics.bounds.width()
            layoutParams.y =windowManager.currentWindowMetrics.bounds.width()/2
        }else{
            layoutParams.x = windowManager.defaultDisplay.width
            layoutParams.y = windowManager.defaultDisplay.height/2
        }

        //将悬浮窗控件添加到WindowManager
        windowManager.addView(viewBinding?.root,layoutParams)
    }

    private fun removeFloatingWindow(){
        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        windowManager.removeView(viewBinding?.root)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        removeFloatingWindow()
        return super.onUnbind(intent)
    }
}