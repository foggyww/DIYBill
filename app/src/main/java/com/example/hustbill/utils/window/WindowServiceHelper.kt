package com.example.hustbill.utils.window

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder


class WindowServiceHelper {

    private var watchingBinder : WindowService.WatchingBinder? = null
    private var watchingConnection : ServiceConnection? = null

    fun connectToService(context: Context,onSuccess: () -> Unit) {
        watchingBinder?.run{
            runOnService(onSuccess)
            return
        }

        watchingConnection = object : ServiceConnection{
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                watchingBinder = service as WindowService.WatchingBinder
                watchingBinder?.run {
                    runOnService(onSuccess)
                }
            }

            override fun onServiceDisconnected(name: ComponentName?) {

            }
        }

        val serviceIntent = Intent(context,WindowService::class.java)
        watchingConnection?.let {
            context.bindService(serviceIntent,it,Context.BIND_AUTO_CREATE)
        }
    }

    fun disconnectToService(context: Context){
        watchingBinder?.let {
            watchingConnection?.let { connect->
                context.unbindService(connect)
                watchingBinder = null
                watchingConnection = null
            }
        }
    }

    fun updateText(text:String){
        watchingBinder?.updateText(text)
    }


}