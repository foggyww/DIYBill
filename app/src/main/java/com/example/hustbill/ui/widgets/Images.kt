package com.example.hustbill.ui.widgets

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun EasyImage(
    modifier: Modifier = Modifier,
    @DrawableRes src: Int,
    contentDescription: String,
    tint: Color? = null,
    alpha: Float = 1.0f,
    scale: ContentScale = ContentScale.Fit,
) {
    if (src != 0) {
        Image(
            painter = painterResource(id = src),
            modifier = modifier,
            contentDescription = contentDescription,
            colorFilter = tint?.let { ColorFilter.tint(it) },
            alpha = alpha,
            contentScale = scale
        )
    }
}