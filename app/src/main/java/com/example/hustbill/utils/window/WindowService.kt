package com.example.hustbill.utils.window

import android.annotation.SuppressLint
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
import com.example.hustbill.R
import com.example.hustbill.databinding.LayoutFloatingImageBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class WindowService: LifecycleService(){

    private val watchingBinder = WatchingBinder()

    inner class WatchingBinder: Binder(){
        fun runOnService(onSuccess:()->Unit){
            lifecycleScope.launch {
                onSuccess.invoke()
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
        return watchingBinder
    }

    // 获取悬浮窗布局
    private var viewBinding: LayoutFloatingImageBinding? = null
    private var windowManager: WindowManager? = null
    private val layoutParams = LayoutParams()

    @SuppressLint("ClickableViewAccessibility", "InflateParams")
    private fun showFloatingWindow() {
        viewBinding = LayoutFloatingImageBinding.inflate(LayoutInflater.from(this))
        //获取WindowManager服务
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        viewBinding?.imgView?.setImageResource(R.mipmap.ic_launcher_round)

        //设置LayoutParam
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            layoutParams.type = LayoutParams.TYPE_PHONE
        }
        layoutParams.format = PixelFormat.RGBA_8888
        layoutParams.gravity = Gravity.START or Gravity.TOP
        layoutParams.flags =
            LayoutParams.FLAG_NOT_TOUCH_MODAL or LayoutParams.FLAG_NOT_FOCUSABLE
        layoutParams.width = LayoutParams.WRAP_CONTENT
        layoutParams.height = LayoutParams.WRAP_CONTENT
        layoutParams.x = 0
        layoutParams.y = 0

        //将悬浮窗控件添加到WindowManager
        windowManager?.addView(viewBinding?.root,layoutParams)
    }

    private fun removeFloatingWindow(){
        windowManager?.removeView(viewBinding?.root)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showFloatingWindow()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        removeFloatingWindow()
        super.onDestroy()
    }

}