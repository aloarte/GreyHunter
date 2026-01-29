package com.devalr.greyhunter.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.devalr.domain.enum.ProgressColorType
import com.devalr.framework.components.progress.ProgressColorStore
import com.devalr.framework.theme.ProgressGreen
import com.devalr.framework.theme.ProgressRed
import com.devalr.framework.theme.ProgressYellow

@Composable
fun InitProgressColors(progressColorType: ProgressColorType) {
    val colors = when (progressColorType) {
        ProgressColorType.Brand -> listOf(MaterialTheme.colorScheme.primary)
        ProgressColorType.TrafficLight -> listOf(
            ProgressRed,
            ProgressYellow,
            ProgressGreen
        )
    }
    ProgressColorStore.updateColors(colors)
}