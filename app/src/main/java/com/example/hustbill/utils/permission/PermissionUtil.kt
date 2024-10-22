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
import com.example.hustbill.MainActivity
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger
import kotlin.properties.Delegates


enum class AppPermission {
    NotificationPermission,
    UsagePermission,
    OverlaysPermission,
    AccessibilityPermission
}

class PermissionUtil(val mainActivity: ComponentActivity) {
    private val appPermissions: MutableMap<AppPermission, Boolean> = SnapshotStateMap()

    init {
        getAllPermission()
    }

    fun getAllPermission() {
        appPermissions[AppPermission.NotificationPermission] = checkNotificationPermission()
        appPermissions[AppPermission.OverlaysPermission] = checkOverlaysPermission()
        appPermissions[AppPermission.UsagePermission] = checkUsagePermission()
        appPermissions[AppPermission.AccessibilityPermission] = checkAccessibilityPermission()
    }


    private fun checkNotificationPermission(): Boolean {
        return NotificationManagerCompat.from(mainActivity).areNotificationsEnabled()
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

    private fun initCheckAllPermission(): Boolean {
        return checkOverlaysPermission() && checkUsagePermission() && checkNotificationPermission()
    }

    fun requestNotifyPermission(
        enableManualRequest: Boolean = true, //是否启用手动申请
    ) {
        //手动申请通知权限
        fun requestNotifyPermissionBySelf() {
            val applicationInfo = mainActivity.applicationInfo
            try {
                val intent = Intent()
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                intent.putExtra("app_package", applicationInfo.packageName)
                intent.putExtra("android.provider.extra.APP_PACKAGE", applicationInfo.packageName)
                intent.putExtra("app_uid", applicationInfo.uid)
                mainActivity.startActivity(intent)
            } catch (e: Exception) {
                val intent = Intent()
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                intent.data = Uri.fromParts("package", applicationInfo.packageName, null)
                mainActivity.startActivity(intent)
            }
        }

        if (appPermissions[AppPermission.NotificationPermission]!! && enableManualRequest) {
            requestNotifyPermissionBySelf()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mainActivity.requestPermission(
                android.Manifest.permission.POST_NOTIFICATIONS,
                {},
                {
                    if (it && enableManualRequest) {
                        requestNotifyPermissionBySelf()
                    }
                }
            )
        } else {
            requestNotifyPermissionBySelf()
        }
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

data class AppInfo @JvmOverloads constructor(
    val appName: String,
    val packageName: String,
    val appIcon: Drawable? = null,
    val time: Long,
)


private val nextLocalRequestCode = AtomicInteger()
private val nextKey: String
    get() = "activity_rq#${nextLocalRequestCode.getAndIncrement()}"

private fun ComponentActivity.requestPermission(
    permission: String,
    onPermit: () -> Unit,
    onDeny: (shouldShowCustomRequest: Boolean) -> Unit,
) {
    if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
        onPermit()
        return
    }
    var launcher by Delegates.notNull<ActivityResultLauncher<String>>()
    launcher = activityResultRegistry.register(
        nextKey,
        ActivityResultContracts.RequestPermission()
    ) { result ->
        if (result) {
            onPermit()
        } else {
            onDeny(!ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
        }
        launcher.unregister()
    }
    lifecycle.addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                launcher.unregister()
                lifecycle.removeObserver(this)
            }
        }
    })
    launcher.launch(permission)
}