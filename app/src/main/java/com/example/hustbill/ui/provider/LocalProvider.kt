package com.example.hustbill.ui.provider

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import com.example.hustbill.utils.permission.PermissionUtil

inline fun <reified T> localProvider(): ProvidableCompositionLocal<T> {
    return compositionLocalOf {
        error("No Local${T::class.simpleName} provided")
    }
}

inline fun <reified T> localStaticProvider(): ProvidableCompositionLocal<T> {
    return staticCompositionLocalOf {
        error("No Local${T::class.simpleName} provided")
    }
}

val LocalPermissionProvider = localProvider<PermissionUtil>()
val LocalNav = localProvider<NavHostController>()

fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}