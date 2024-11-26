package com.example.diybill.ui.widgets

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.diybill.App
import com.example.diybill.ui.provider.LocalNav
import com.example.diybill.ui.provider.LocalShowImage
import com.example.diybill.utils.AppCache
import com.example.diybill.utils.AppFile
import com.example.diybill.utils.clickNoRepeat
import okio.FileSystem

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
fun MemCacheImage(modifier: Modifier,
                  path: String,
                  scale: ContentScale = ContentScale.Crop,
                  alignment: Alignment = Alignment.Center,
                  clickAble:Boolean = true){
    val realUrl = remember(path){
        AppFile.getUri("images/$path")
            ?: AppCache.getUri(path)
    }
    val showImageProvider = LocalShowImage.current
    AsyncImage(
        modifier = if(clickAble) modifier.clickNoRepeat {
            showImageProvider.showImage(path)
        } else modifier,
        model = ImageRequest.Builder(App.CONTEXT)
            .data(realUrl)
            .allowHardware(true)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCacheKey(realUrl)
            .build(),
        contentDescription = "图片",
        contentScale = scale,
        alignment = alignment,
        imageLoader = imageLoader
    )
}

private val imageLoader = ImageLoader.Builder(App.CONTEXT)
    .crossfade(true)
    .allowHardware(true)
    .memoryCachePolicy(CachePolicy.ENABLED)
    .memoryCache(
        MemoryCache.Builder(App.CONTEXT)
            .strongReferencesEnabled(true)
            .weakReferencesEnabled(true)
            .maxSizePercent(0.2)
            .build()
    )
    .build()