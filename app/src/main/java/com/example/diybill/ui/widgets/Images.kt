package com.example.diybill.ui.widgets

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.diybill.App
import com.example.diybill.utils.AppCache
import com.example.diybill.utils.AppFile
import com.example.diybill.utils.getAppCacheFolder
import com.example.diybill.utils.getAppFile

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

@Composable
fun FileImage(modifier: Modifier,
              path: String){
    AsyncImage(
        modifier=Modifier,
        model = ImageRequest.Builder(App.CONTEXT)
            .data(AppFile.getUri("images/$path"))
            .build(),
        contentDescription = "file区图片",
        contentScale = ContentScale.Crop,
        alignment = Alignment.Center
    )
}

@Composable
fun CacheImage(
    modifier: Modifier,
    path: String,
) {
    AsyncImage(
        modifier = modifier,
        model = AppCache.getUri(path),
        contentDescription = "cache区图片",
        contentScale = ContentScale.Crop,
        alignment = Alignment.Center
    )
}