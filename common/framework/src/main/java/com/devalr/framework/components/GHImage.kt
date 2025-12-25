package com.devalr.framework.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
    imageUri?.let {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUri.toUri())
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.loading_image),
            error = painterResource(R.drawable.error_image),
            fallback = painterResource(R.drawable.add_image),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = modifier
                .size(size)
                .clip(RoundedCornerShape(borderRadius))
                .then(
                    if (onImageClick != null) {
                        Modifier.clickable { onImageClick.invoke() }
                    } else {
                        Modifier
                    }
                )
        )

    } ?: run {
        Box(
            modifier = modifier
                .background(Color.LightGray)
                .then(
                    if (onImageClick != null) {
                        Modifier.clickable { onImageClick.invoke() }
                    } else {
                        Modifier
                    }
                )) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(80.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(imageVector = Icons.Default.Create, contentDescription = "")
                GHText(text = "Add a picture", type = TextType.LabelM)

            }
        }


    }
}

@Preview
@Composable
private fun GHImagePreview() {
    GHImage(imageUri = null, size = 200.dp)
}
