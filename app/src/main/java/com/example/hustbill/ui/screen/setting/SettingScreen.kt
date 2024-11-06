package com.example.hustbill.ui.screen.setting

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bingyan.focuscat.ui.widgets.background.MainBackground
import com.example.hustbill.App
import com.example.hustbill.config.Config
import com.example.hustbill.ui.provider.LocalPermissionProvider
import com.example.hustbill.ui.theme.AppTypography
import com.example.hustbill.ui.theme.Gap
import com.example.hustbill.ui.theme.colors
import com.example.hustbill.ui.theme.white
import com.example.hustbill.ui.widgets.AppSwitch
import com.example.hustbill.ui.widgets.TextHeader
import com.example.hustbill.ui.widgets.TitleCard
import com.example.hustbill.utils.permission.AppPermission
import com.example.hustbill.utils.watching.WatchUsageUtil
import com.example.hustbill.utils.watching.WindowServiceHelper
import kotlinx.coroutines.delay

val stringList = mutableStateListOf<String>()

enum class AutoType(text: String) {
    AutoRecord("自动记账"),
    OVER("悬浮窗"),
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun SettingScreen(
    contentPadding: PaddingValues,
    vm: SettingViewModel = viewModel(),
) {

    val permissionUtil = LocalPermissionProvider.current
    val context = LocalContext.current
    val stringList = remember {
        stringList
    }

    var permissionMap by remember {
        mutableStateOf(permissionUtil.getPermissions())
    }

    LaunchedEffect(Unit) {
        while (true){
            delay(2000)
            permissionMap = permissionUtil.getPermissions()
        }
    }

    val autoMap = remember {
       SnapshotStateMap<AutoType,Boolean>().apply {
           put(AutoType.OVER, Config.openOver)
       }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        TextHeader(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = Gap.Big),
            text = "Settings"
        )
//            TitleCard(
//                modifier = Modifier.fillMaxWidth(),
//                title = "账本设置"
//            ) {
//
//            }
        TitleCard(
            modifier = Modifier.fillMaxWidth(),
            title = "权限设置"
        ) {
            SwitchItem(
                text = "悬浮窗",
                selected = permissionMap[AppPermission.OverlaysPermission]!!
            ) {
                permissionUtil.requestOverlaysPermission()
            }
            SwitchItem(
                text = "设备使用情况",
                selected = permissionMap[AppPermission.UsagePermission]!!
            ) {
                permissionUtil.requestUsagePermission()
            }

        }
        Spacer(modifier = Modifier.height(Gap.Large))
        TitleCard(
            modifier = Modifier.fillMaxWidth(),
            title = "功能设置"
        ) {
            SwitchItem(
                text = "自动记账",
                selected = permissionMap[AppPermission.AccessibilityPermission]!!
            ) {
                permissionUtil.requestAccessibilityPermission()
            }
            SwitchItem(
                text = "悬浮窗提示",
                selected = autoMap[AutoType.OVER]!!
            ) {
                Config.openOver = !Config.openOver
                autoMap[AutoType.OVER] = Config.openOver
                if (autoMap[AutoType.OVER]!!) {
                    vm.openOver(context)
                } else {
                    vm.closeOver(context)
                }
            }
        }
        LazyColumn {
            item {
                for (s in stringList) {
                    Text(text = s)
                }
            }
        }
    }
}

@Composable
private fun SwitchItem(
    text: String,
    msg: String? = null,
    selected: Boolean,
    onSelect: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text, style = AppTypography.smallMsg, color = colors.textSecondary)
        msg?.let {
            Spacer(modifier = Modifier.width(Gap.Small))
            Text(it, style = AppTypography.smallMsg, color = colors.unfocusedSecondary)
        }
        Spacer(modifier = Modifier.weight(1f))
        AppSwitch(checked = selected,
            modifier = Modifier.scale(0.8f)) {
            onSelect()
        }
    }
}