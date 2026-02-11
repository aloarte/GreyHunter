package com.devalr.painting.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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
import com.devalr.framework.components.ScreenSize
import com.devalr.framework.components.button.GHButton
import com.devalr.framework.components.button.TopButtons
import com.devalr.framework.components.gh.GHText
import com.devalr.framework.components.gh.TextType
import com.devalr.framework.components.markedtext.MarkedText
import com.devalr.framework.components.rememberScreenSize
import com.devalr.framework.theme.GreyHunterTheme
import com.devalr.painting.R

@Composable
fun PaintingScreenPortraitContent(
    innerPadding: PaddingValues = PaddingValues(),
    miniatures: List<MiniatureBo>,
    seconds: Int,
    timerStopped: Boolean,
    onToggleTimer: () -> Unit,
    onNavigateBack: () -> Unit,
    onFinishPainting: () -> Unit
) {
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
                type = if (rememberScreenSize() == ScreenSize.COMPACT)
                    TextType.Featured else TextType.UltraFeatured,
                textColor = Color.White
            )
            Spacer(modifier = Modifier.height(30.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                GHButton(
                    text = if (timerStopped)
                        stringResource(R.string.btn_resume_timer)
                    else stringResource(R.string.btn_stop_timer),
                    invertColors = true,
                    onClick = onToggleTimer
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

@Preview(showBackground = true)
@Composable
private fun PaintingScreenContentLightModePreview() {
    GreyHunterTheme(darkTheme = false) {
        PaintingScreenPortraitContent(
            seconds = 30,
            timerStopped = false,
            miniatures = listOf(
                technomancer,
                chronomancer,
                immortal,
                deathmark
            ),
            onToggleTimer = {
                // Do nothing
            },
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
        PaintingScreenPortraitContent(
            seconds = 30,
            timerStopped = false,
            miniatures = listOf(
                technomancer,
                chronomancer,
                immortal,
                deathmark
            ),
            onToggleTimer = {
                // Do nothing
            },
            onNavigateBack = {
                // Do nothing
            },
            onFinishPainting = {
                // Do nothing
            }
        )
    }
}