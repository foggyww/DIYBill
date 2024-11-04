package com.example.hustbill.ui.screen.index

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import com.example.hustbill.ui.screen.add.AddScreen
import com.example.hustbill.ui.screen.home.MainScreen
import com.example.hustbill.ui.screen.setting.SettingScreen
import com.example.hustbill.ui.theme.colors
import com.example.hustbill.ui.widgets.TitleSpacer


@SuppressLint("SuspiciousIndentation")
@Composable
fun AppScaffold(
    contentPadding:PaddingValues
) {
    val state = rememberPagerState { 4 }
    Scaffold(
        topBar = {
            TitleSpacer()
        },
        bottomBar = {
            BottomNavBar(page = state)
        }
    ){ padding->
        HorizontalPager(
            state=state,
            userScrollEnabled = false,
            modifier = Modifier.background(colors.geryBackground)
                .fillMaxSize()
                .padding(padding)
        ) { page->
            when(page){
                0 -> MainScreen(contentPadding)
                3 -> SettingScreen(contentPadding)
            }
        }
    }

}