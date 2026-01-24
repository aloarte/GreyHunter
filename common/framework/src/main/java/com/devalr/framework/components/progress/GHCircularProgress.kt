package com.devalr.framework.components.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.framework.theme.GreyHunterTheme
import com.devalr.framework.theme.ProgressGreen
import com.devalr.framework.theme.ProgressRed
import com.devalr.framework.theme.ProgressYellow

@Composable
fun GHCircularProgress(
    modifier: Modifier = Modifier,
    percentage: Float,
    spectrum: List<Color> = listOf(MaterialTheme.colorScheme.primary)
) {
    val safePercentage = percentage.coerceIn(0f, 1f)
    val targetColor = calculateColorForPercentage(safePercentage, spectrum)
    CircularProgressIndicator(
        progress = { percentage },
        modifier = modifier,
        color = targetColor,
        trackColor = MaterialTheme.colorScheme.outlineVariant,
        strokeWidth = 3.dp
    )
}

@Preview(showBackground = true)
@Composable
private fun GHCircularProgressDefaultColorsDarkTheme() {
    GreyHunterTheme(darkTheme = true) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp),
            horizontalArrangement = spacedBy(1.dp)
        ) {
            for (i in 0..10) {
                GHCircularProgress(percentage = i / 10f)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GHCircularProgressDefaultColorsLightTheme() {
    GreyHunterTheme(darkTheme = false) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp),
            horizontalArrangement = spacedBy(1.dp)
        ) {
            for (i in 0..10) {
                GHCircularProgress(percentage = i / 10f)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GHCircularProgressTrafficLightsColorsDarkTheme() {
    GreyHunterTheme(darkTheme = true) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp),
            horizontalArrangement = spacedBy(1.dp)
        ) {
            for (i in 0..10) {
                GHCircularProgress(
                    percentage = i / 10f,
                    spectrum = listOf(ProgressRed, ProgressYellow, ProgressGreen)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GHCircularProgressTrafficLightsColorsLightTheme() {
    GreyHunterTheme(darkTheme = false) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp),
            horizontalArrangement = spacedBy(1.dp)
        ) {
            for (i in 0..10) {
                GHCircularProgress(
                    percentage = i / 10f,
                    spectrum = listOf(ProgressRed, ProgressYellow, ProgressGreen)
                )
            }
        }
    }
}

