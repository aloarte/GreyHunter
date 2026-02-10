package com.devalr.painting.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.helpers.chronomancer
import com.devalr.domain.model.helpers.deathmark
import com.devalr.domain.model.helpers.immortal
import com.devalr.domain.model.helpers.technomancer
import com.devalr.framework.components.button.GHButton
import com.devalr.framework.components.gh.GHText
import com.devalr.framework.components.ScreenSize
import com.devalr.framework.components.gh.TextType
import com.devalr.framework.components.button.TopButtons
import com.devalr.framework.components.markedtext.MarkedText
import com.devalr.framework.components.rememberScreenSize
import com.devalr.framework.theme.GreyHunterTheme
import com.devalr.painting.R
import kotlinx.coroutines.delay

@Composable
fun PaintingScreenContent(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    miniatures: List<MiniatureBo>,
    onNavigateBack: () -> Unit,
    onFinishPainting: () -> Unit
) {
    var seconds by remember { mutableIntStateOf(0) }
    var timerStopped by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            if (timerStopped.not()) {
                seconds++
            }
        }
    }

    PreloadMiniatureImages(miniatures)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(innerPadding)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize()
                .padding(
                    horizontal = if (rememberScreenSize() == ScreenSize.COMPACT) 20.dp else 40.dp,
                    vertical = 40.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            MarkedText(
                text = stringResource(R.string.painting_message),
                textColor = Color.White,
                color = MaterialTheme.colorScheme.background,
                barsSize = 50.dp
            )
            Spacer(modifier = Modifier.height(10.dp))
            PaintingMinisCarousel(miniatures = miniatures)
            Spacer(modifier = Modifier.height(10.dp))
            GHText(
                text = formatSecondsToHHMMSS(seconds),
                type = if (rememberScreenSize() == ScreenSize.COMPACT) TextType.Featured else TextType.UltraFeatured,
                textColor = Color.White
            )
            Spacer(modifier = Modifier.height(30.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                GHButton(
                    text = if (timerStopped) stringResource(R.string.btn_resume_timer)
                    else stringResource(R.string.btn_stop_timer),
                    invertColors = true,
                    onClick = { timerStopped = !timerStopped }
                )
                GHButton(
                    text = stringResource(R.string.btn_stop_painting),
                    invertColors = true,
                    onClick = onFinishPainting
                )
            }

        }
        TopButtons(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(16.dp),
            onNavigateBack = onNavigateBack
        )
    }
}

private fun formatSecondsToHHMMSS(totalSeconds: Int): String {
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