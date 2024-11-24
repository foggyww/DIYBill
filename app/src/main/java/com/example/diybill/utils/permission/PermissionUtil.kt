package com.example.diybill.utils.permission

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.diybill.MainActivity
import kotlinx.coroutines.launch


enum class AppPermission {
    UsagePermission,
    OverlaysPermission,
    AccessibilityPermission
}

class PermissionUtil(private val mainActivity: ComponentActivity) {
    private val appPermissions: MutableMap<AppPermission, Boolean> = mutableMapOf()

    init {
        getAllPermission()
    }

    private fun getAllPermission() {
        appPermissions[AppPermission.OverlaysPermission] = checkOverlaysPermission()
        appPermissions[AppPermission.UsagePermission] = checkUsagePermission()
        appPermissions[AppPermission.AccessibilityPermission] = checkAccessibilityPermission()
    }

    fun getPermissions():Map<AppPermission,Boolean>{
        getAllPermission()
        return appPermissions.toMap()
    }

    private fun checkUsagePermission(): Boolean {
        val appOpsManager = mainActivity.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOpsManager.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            android.os.Process.myUid(),
            mainActivity.packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }

    private fun checkOverlaysPermission(): Boolean {
        return Settings.canDrawOverlays(mainActivity)
    }

    private fun checkAccessibilityPermission():Boolean{
        var accessibilityEnabled = 0

        try {
            accessibilityEnabled = Settings.Secure.getInt(
                mainActivity.contentResolver,
                Settings.Secure.ACCESSIBILITY_ENABLED
            )
        } catch (e: SettingNotFoundException) {
            return false
        }
        if(accessibilityEnabled==1){
            return true
        }else{
            return false
        }
    }

    fun requestAccessibilityPermission() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        mainActivity.startActivity(intent)
    }

    fun requestUsagePermission() {
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        intent.data = Uri.parse("package:${mainActivity.packageName}")
        val lifecycle = mainActivity
        val activity = lifecycle.lifecycleScope.launchWhenResumed {
            mainActivity.startActivity(intent)
        }
        lifecycle.lifecycleScope.launch {
            activity.join()
        }
    }

    fun requestOverlaysPermission() {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        intent.data = Uri.parse("package:${mainActivity.packageName}")
        val lifecycle = (mainActivity as MainActivity)
        val activity = lifecycle.lifecycleScope.launchWhenResumed {
            mainActivity.startActivity(intent)
        }
        lifecycle.lifecycleScope.launch {
            activity.join()
        }
    }
}