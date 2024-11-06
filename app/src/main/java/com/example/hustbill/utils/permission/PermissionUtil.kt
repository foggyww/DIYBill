package com.example.hustbill.utils.permission

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.hustbill.App
import com.example.hustbill.MainActivity
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger
import kotlin.properties.Delegates


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