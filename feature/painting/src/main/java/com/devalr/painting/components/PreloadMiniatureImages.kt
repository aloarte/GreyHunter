package com.devalr.painting.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.size.Size
import com.devalr.domain.model.MiniatureBo

@Composable
fun PreloadMiniatureImages(miniatures: List<MiniatureBo>) {
    val context = LocalContext.current
    val imageLoader = context.imageLoader

    LaunchedEffect(miniatures) {
        miniatures.forEach { miniature ->
            val request = ImageRequest.Builder(context)
                .data(miniature.imageUri)
                .size(Size.ORIGINAL)
                .build()

            imageLoader.enqueue(request)
        }
    }
}
