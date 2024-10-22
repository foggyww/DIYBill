package com.example.hustbill

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import com.bingyan.focuscat.ui.widgets.background.MainBackground
import com.example.hustbill.ui.provider.LocalPermissionProvider
import com.example.hustbill.ui.theme.AppTheme
import com.example.hustbill.utils.permission.PermissionUtil
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            CompositionLocalProvider(
                LocalPermissionProvider provides PermissionUtil(this)
            ) {
                val nav = rememberAnimatedNavController()
                AppTheme {
                    MainBackground{
                        AppNav(nav)
                    }
                }
            }
        }
    }
}


