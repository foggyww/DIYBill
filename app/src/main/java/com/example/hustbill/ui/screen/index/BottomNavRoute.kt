package com.example.hustbill.ui.screen.index

import androidx.annotation.DrawableRes
import com.example.hustbill.R

sealed class BottomNavRoute(
    val routeNumber: Int,
    val string: String,
    @DrawableRes var icon: Int,
    @DrawableRes var iconSelected: Int = icon
) {
    data object Home : BottomNavRoute(
        NavRoute.Home,
        "主页",
        R.drawable.home,
    )

    data object Type :
        BottomNavRoute(
            NavRoute.Type,
            "分类",
            R.drawable.type,
        )

    data object Advices :
        BottomNavRoute(
            NavRoute.Advices,
            "建议",
            R.drawable.advices,
        )

    data object Setting :
        BottomNavRoute(
            NavRoute.Setting,
            "设置",
            R.drawable.setting
        )

    data object Add :
        BottomNavRoute(NavRoute.Add,"添加", R.drawable.add_yellow)
}

object NavRoute {
    const val Home = 0
    const val Type = 1
    const val Advices = 2
    const val Setting = 3
    const val Add = -1
}