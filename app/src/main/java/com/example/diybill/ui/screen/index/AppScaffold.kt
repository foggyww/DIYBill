package com.example.diybill.ui.screen.index

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.diybill.ui.screen.category.CategoryScreen
import com.example.diybill.ui.screen.home.MainScreen
import com.example.diybill.ui.screen.setting.SettingScreen
import com.example.diybill.ui.screen.support.SupportScreen
import com.example.diybill.ui.theme.colors
import com.example.diybill.ui.widgets.TitleSpacer


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
                1 -> CategoryScreen(contentPadding)
                2 -> SupportScreen(contentPadding)
                3 -> SettingScreen(contentPadding)
            }
        }
    }

}