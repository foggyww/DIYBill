package com.example.diybill.base

import android.content.res.Resources
import androidx.activity.ComponentActivity

open class BaseActivity: ComponentActivity() {
    //防止字体随系统变化
    override fun getResources(): Resources {
        val resources = super.getResources()
        if(resources!=null&&resources.configuration.fontScale!=1.0f){
            val config = resources.configuration
            config.fontScale = 1.0f
            return createConfigurationContext(config).resources
        }
        return resources
    }
}