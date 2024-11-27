package com.example.diybill

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import com.bingyan.focuscat.ui.widgets.background.MainBackground
import com.example.diybill.base.BaseActivity
import com.example.diybill.ui.provider.LocalNav
import com.example.diybill.ui.provider.LocalPermissionProvider
import com.example.diybill.ui.provider.LocalPicker
import com.example.diybill.ui.provider.LocalShowImage
import com.example.diybill.ui.provider.Picker
import com.example.diybill.ui.provider.ShowImageProvider
import com.example.diybill.ui.theme.AppTheme
import com.example.diybill.utils.permission.PermissionUtil
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

class MainActivity : BaseActivity(){

    private val piker = registerForActivityResult(Picker()){}

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val nav = rememberAnimatedNavController()
            CompositionLocalProvider(
                LocalPermissionProvider provides PermissionUtil(this),
                LocalNav provides nav,
                LocalPicker provides piker,
                LocalShowImage provides ShowImageProvider()
            ) {
                AppTheme {
                    LocalShowImage.current.Build {
                        MainBackground{
                            AppNav(nav)
                        }
                    }
                }
            }
        }
    }
}


