package com.example.hustbill.ui.screen.setting

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bingyan.focuscat.ui.widgets.background.MainBackground
import com.example.hustbill.ui.provider.LocalPermissionProvider
import com.example.hustbill.ui.screen.home.MainViewModel
import com.example.hustbill.ui.theme.Gap
import com.example.hustbill.ui.widgets.TextHeader
import com.example.hustbill.ui.widgets.TitleCard
import com.example.hustbill.utils.watching.WatchUsageUtil
import com.example.hustbill.utils.watching.WindowServiceHelper
import kotlinx.coroutines.delay

val stringList =mutableStateListOf<String>()

@SuppressLint("SuspiciousIndentation")
@Composable
fun SettingScreen(contentPadding:PaddingValues) {
        Column(modifier = Modifier.fillMaxSize()
            .padding(contentPadding)) {
            TextHeader(modifier = Modifier.fillMaxWidth()
                .padding(vertical = Gap.Big),
                text = "Settings")
            TitleCard(
                modifier = Modifier.fillMaxWidth(),
                title = "账本设置"
            ) {

            }
            val permissionUtil = LocalPermissionProvider.current
            val context = LocalContext.current
            val stringList = remember {
                stringList
            }
            MainBackground {
                Button(onClick = {
                    permissionUtil.requestUsagePermission()
                }) { }
                Button(onClick = {
                    permissionUtil.requestOverlaysPermission()
                }) { }

                Button(onClick = {
                    permissionUtil.requestAccessibilityPermission()
                }) { }
                Button(onClick = {
                    val win = WindowServiceHelper()
                    win.connectToService(context) {
                        while (true) {
                            delay(1000)
                            val list = WatchUsageUtil.getUsageStats(context)
                            if(list.isNotEmpty()){
                                val s=list.firstOrNull()?.packageName+"\n"+list.firstOrNull()?.className
                                win.updateText(s)
                                stringList.add(s)
                                Log.i("TEST",s)
                            }
                        }
                    }
                }) {

                }
                LazyColumn {
                    item {
                        for (s in stringList){
                            Text(text = s)
                        }
                    }
                }
            }
        }

}