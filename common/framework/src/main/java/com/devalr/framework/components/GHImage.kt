package com.devalr.framework.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.devalr.framework.R

@Composable
fun GHImage(
    modifier: Modifier = Modifier,
    imageUri: String?,
    size: Dp,
    borderRadius: Dp = 20.dp,
    onImageClick: (() -> Unit)? = null
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(if (!imageUri.isNullOrBlank()) imageUri.toUri() else null)
            .crossfade(true)
            .build(),
        placeholder = painterResource(R.drawable.loading_image),
        error = painterResource(R.drawable.error_image),
        fallback = painterResource(R.drawable.add_image),
        contentDescription = "",
        contentScale = ContentScale.Fit,
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(borderRadius))
                then (
                if (onImageClick != null) {
                    Modifier.clickable { onImageClick.invoke() }
                } else {
                    Modifier
                }
                )
    )
}

@Preview
@Composable
private fun GHImagePreview() {
    GHImage(imageUri = null, size = 40.dp)
}