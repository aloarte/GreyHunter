package com.devalr.framework.components.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun VerticalProgress(
    modifier: Modifier = Modifier,
    progress: Float,
    spectrum: List<Color> = listOf(Color.Red, Color.Yellow, Color.Green),
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(8.dp)
            .background(calculateColorForPercentage(progress, spectrum))
    )
}