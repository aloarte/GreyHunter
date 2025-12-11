package com.devalr.framework.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.devalr.framework.R
import java.io.File

@Composable
fun GHImage(imageUri:String?) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(
                if (!imageUri.isNullOrBlank()) File(imageUri) else null
            )
            .crossfade(true)
            .build(),
        placeholder = painterResource(R.drawable.default_project_image),
        error = painterResource(R.drawable.default_project_image),
        fallback = painterResource(R.drawable.default_project_image),
        contentDescription = "",
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .fillMaxWidth(0.3f)
            .aspectRatio(1.6f)
    )
}

@Preview
@Composable
private fun GHImagePreview(){
    GHImage(imageUri = null)
}