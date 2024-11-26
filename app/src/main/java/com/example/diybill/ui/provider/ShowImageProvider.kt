package com.example.diybill.ui.provider

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import com.example.diybill.ui.theme.colors
import com.example.diybill.ui.widgets.MemCacheImage
import kotlin.math.absoluteValue

class ShowImageProvider{

    private val showImage = mutableStateOf(false)
    private val url: MutableState<String?> = mutableStateOf(null)
    fun showImage(path: String?) {
        path?.let {
            showImage.value = true
            url.value = path
        }
    }


    @Composable
    fun Build(content: @Composable () -> Unit) {
        val show = remember {
            showImage
        }
        content()
        url.value?.let {
            FullScreenImage(url = it,
                show = show,
                onClick = {
                    show.value = false
                }
            )
        }
    }

    /**
     * 全屏图片查看
     * @param modifier Modifier
     * @param url String
     */
    @Composable
    fun FullScreenImage(
        modifier: Modifier = Modifier,
        show: MutableState<Boolean>,
        url: String,
        onClick: () -> Unit = {},
    ) {

        var scale by remember { mutableFloatStateOf(1f) }
        var offset by remember { mutableStateOf(Offset.Zero) }
        var imageSize by remember {
            mutableStateOf(Size.Unspecified)
        }
        val state = rememberTransformableState { zoomChange, _, _ ->
            scale = (zoomChange * scale).coerceAtLeast(1f)
//            offset += panChange
        }
        if (show.value) {
            BackHandler {
                show.value = false
            }
            Surface(
                color = colors.textPrimary,
                modifier = modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = { point ->
                                val center = Offset(imageSize.width / 2, imageSize.height / 2)
                                val realPoint = offset + center - point
                                scale = if (scale <= 1f) 2f else 1f
                                offset = if (scale <= 1f) Offset.Zero else {
                                    calOffset(imageSize, scale, realPoint * 2f)
                                }
                            },
                            onTap = {
                                onClick.invoke()
                            },
                        )
                    }
            ) {
                MemCacheImage(
                    path = url,
                    modifier = Modifier
                        .fillMaxSize()
                        .transformable(state = state)
                        .graphicsLayer {
                            imageSize = size
                            scaleX = scale
                            scaleY = scale
                            translationX = offset.x
                            translationY = offset.y
                        }
                        .pointerInput(Unit) {
                            detectDragGestures { _, dragAmount ->
                            }
                        },
                    clickAble = false,
                    scale = ContentScale.Fit
                )
            }
        }
    }

    private fun calOffset(
        imgSize: Size,
        scale: Float,
        offsetChanged: Offset,
        isInvalid: (Boolean) -> Unit = {},
    ): Offset {
        if (imgSize == Size.Unspecified) return Offset.Zero
        val px = imgSize.width * (scale - 1f) / 2f
        val py = imgSize.height * (scale - 1f) / 2f
        var np = offsetChanged
        val xDiff = np.x.absoluteValue - px
        val yDiff = np.y.absoluteValue - py
        if (xDiff > 0)
            np = np.copy(x = px * np.x.absoluteValue / np.x)
        if (yDiff > 0)
            np = np.copy(y = py * np.y.absoluteValue / np.y)
        isInvalid(xDiff > 0 && xDiff > yDiff)
        return np
    }

}