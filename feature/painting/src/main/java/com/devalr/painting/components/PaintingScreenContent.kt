package com.devalr.painting.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.helpers.chronomancer
import com.devalr.domain.model.helpers.deathmark
import com.devalr.domain.model.helpers.immortal
import com.devalr.domain.model.helpers.technomancer
import com.devalr.framework.theme.GreyHunterTheme
import kotlinx.coroutines.delay

@Composable
fun PaintingScreenContent(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    miniatures: List<MiniatureBo>,
    onNavigateBack: () -> Unit,
    onFinishPainting: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape =
        configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    var seconds by remember { mutableIntStateOf(0) }
    var timerStopped by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            if (!timerStopped) seconds++
        }
    }

    if (isLandscape) {
        PaintingScreenLandscapeContent(
            innerPadding = innerPadding,
            miniatures = miniatures,
            seconds = seconds,
            timerStopped = timerStopped,
            onToggleTimer = { timerStopped = !timerStopped },
            onNavigateBack = onNavigateBack,
            onFinishPainting = onFinishPainting
        )
    } else {
        PaintingScreenPortraitContent(
            innerPadding = innerPadding,
            miniatures = miniatures,
            seconds = seconds,
            timerStopped = timerStopped,
            onToggleTimer = { timerStopped = !timerStopped },
            onNavigateBack = onNavigateBack,
            onFinishPainting = onFinishPainting
        )
    }
}

fun formatSecondsToHHMMSS(totalSeconds: Int): String {
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return if (hours > 0) {
        "%02d:%02d:%02d".format(hours, minutes, seconds)
    } else {
        "%02d:%02d".format(minutes, seconds)
    }
}

@Preview(showBackground = true)
@Composable
private fun PaintingScreenContentLightModePreview() {
    GreyHunterTheme(darkTheme = false) {
        PaintingScreenContent(
            miniatures = listOf(
                technomancer,
                chronomancer,
                immortal,
                deathmark
            ),
            onNavigateBack = {
                // Do nothing
            },
            onFinishPainting = {
                // Do nothing
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PaintingScreenContentDarkModePreview() {
    GreyHunterTheme(darkTheme = true) {
        PaintingScreenContent(
            miniatures = listOf(
                technomancer,
                chronomancer,
                immortal,
                deathmark
            ),
            onNavigateBack = {
                // Do nothing
            },
            onFinishPainting = {
                // Do nothing
            }
        )
    }
}