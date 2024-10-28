package com.example.hustbill.utils

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.click(click: () -> Unit) = composed {
    val lastClick = remember { mutableLongStateOf(0L) }
    Modifier.clickable(
        onClick = {
            val now = System.currentTimeMillis()
            if (now - lastClick.longValue > 500L) {
                click()
                lastClick.longValue = now
            }
        },
        indication = rememberRipple(),
        interactionSource = remember { MutableInteractionSource() }
    )
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.clickNoRepeat(
    color: Color = Color.Unspecified,
    repeatDuring: Long = 500,
    click: () -> Unit
) = composed {
    val lastClick = remember { mutableStateOf(0L) }
    Modifier.clickable(
        onClick = {
            val now = System.currentTimeMillis()
            if (now - lastClick.value > repeatDuring) {
                click()
                lastClick.value = now
            }
        },
        indication = rememberRipple(color = color),
        interactionSource = remember { MutableInteractionSource() }
    )
}
