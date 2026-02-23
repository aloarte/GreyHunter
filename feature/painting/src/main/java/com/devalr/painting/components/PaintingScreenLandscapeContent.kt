package com.devalr.painting.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.domain.model.MiniatureBo
import com.devalr.domain.model.helpers.chronomancer
import com.devalr.domain.model.helpers.deathmark
import com.devalr.domain.model.helpers.immortal
import com.devalr.domain.model.helpers.technomancer
import com.devalr.framework.PAINTING_BUTTON
import com.devalr.framework.components.button.GHButton
import com.devalr.framework.components.button.TopButtons
import com.devalr.framework.components.gh.GHText
import com.devalr.framework.components.gh.TextType
import com.devalr.framework.components.markedtext.MarkedText
import com.devalr.framework.theme.GreyHunterTheme
import com.devalr.painting.R

@Composable
fun PaintingScreenLandscapeContent(
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
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp, vertical = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                MarkedText(
                    text = stringResource(R.string.painting_message),
                    textColor = Color.White,
                    color = MaterialTheme.colorScheme.background,
                    barsSize = 50.dp
                )

                Spacer(modifier = Modifier.height(20.dp))
                GHText(
                    text = formatSecondsToHHMMSS(seconds),
                    type = TextType.Featured,
                    textColor = Color.White
                )
                Spacer(modifier = Modifier.height(40.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    GHButton(
                        text = if (timerStopped)
                            stringResource(R.string.btn_resume_timer)
                        else stringResource(R.string.btn_stop_timer),
                        invertColors = true,
                        onClick = onToggleTimer
                    )
                    GHButton(
                        modifier = Modifier.semantics { contentDescription = PAINTING_BUTTON },
                        text = stringResource(R.string.btn_stop_painting),
                        invertColors = true,
                        onClick = onFinishPainting
                    )
                }
            }

            Spacer(modifier = Modifier.width(40.dp))
            Box(
                modifier = Modifier
                    .weight(1.5f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                PaintingMinisCarousel(
                    miniatures = miniatures,
                    modifier = Modifier.fillMaxSize()
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

@Preview(
    name = "Landscape",
    showBackground = true,
    widthDp = 891,
    heightDp = 411
)
@Composable
private fun PaintingScreenLandscapeContentLightModePreview() {
    GreyHunterTheme(darkTheme = false) {
        PaintingScreenLandscapeContent(
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

@Preview(
    name = "Landscape",
    showBackground = true,
    widthDp = 891,
    heightDp = 411
)
@Composable
private fun PaintingScreenLandscapeContentDarkModePreview() {
    GreyHunterTheme(darkTheme = true) {
        PaintingScreenLandscapeContent(
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

@Preview(
    name = "Landscape small",
    showBackground = true,
    widthDp = 640,
    heightDp = 360
)
@Composable
private fun PaintingScreenLandscapeContentLightModePreviewSmall() {
    GreyHunterTheme(darkTheme = false) {
        PaintingScreenLandscapeContent(
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

@Preview(
    name = "Landscape small",
    showBackground = true,
    widthDp = 640,
    heightDp = 360
)
@Composable
private fun PaintingScreenLandscapeContentDarkModePreviewSmall() {
    GreyHunterTheme(darkTheme = true) {
        PaintingScreenLandscapeContent(
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