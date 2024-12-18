package com.example.diybill.ui.screen.setting

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.diybill.R
import com.example.diybill.config.Config
import com.example.diybill.ui.provider.LocalPermissionProvider
import com.example.diybill.ui.theme.AppTypography
import com.example.diybill.ui.theme.Gap
import com.example.diybill.ui.theme.ImageSize
import com.example.diybill.ui.theme.RoundedShapes
import com.example.diybill.ui.theme.colors
import com.example.diybill.ui.widgets.AppSwitch
import com.example.diybill.ui.widgets.EasyImage
import com.example.diybill.ui.widgets.TextHeader
import com.example.diybill.ui.widgets.TitleCard
import com.example.diybill.utils.permission.AppPermission
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
        Box(modifier = Modifier.fillMaxWidth()
            .height(120.dp),
            contentAlignment = Alignment.Center){
            EasyImage(
                src = R.mipmap.ic_launcher_original,
                contentDescription = "应用图标",
                modifier = Modifier.fillMaxHeight()
                    .aspectRatio(1f)
                    .clip(RoundedShapes.large)
            )
        }
        Spacer(modifier = Modifier.height(Gap.Large))
//        TitleCard(
//            modifier = Modifier.fillMaxWidth(),
//            title = "权限设置"
//        ) {
//            SwitchItem(
//                text = "悬浮窗",
//                selected = permissionMap[AppPermission.OverlaysPermission]!!
//            ) {
//                permissionUtil.requestOverlaysPermission()
//            }
//            SwitchItem(
//                text = "设备使用情况",
//                selected = permissionMap[AppPermission.UsagePermission]!!
//            ) {
//                permissionUtil.requestUsagePermission()
//            }
//
//        }
//        Spacer(modifier = Modifier.height(Gap.Large))
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