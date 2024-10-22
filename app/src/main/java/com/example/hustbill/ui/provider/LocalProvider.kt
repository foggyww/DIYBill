package com.example.hustbill.ui.provider

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
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