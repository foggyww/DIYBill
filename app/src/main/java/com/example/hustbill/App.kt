package com.example.hustbill

import android.app.Application
import android.content.Context

class App:Application() {
    companion object {
        lateinit var CONTEXT: Application
    }
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        CONTEXT = this
    }
}