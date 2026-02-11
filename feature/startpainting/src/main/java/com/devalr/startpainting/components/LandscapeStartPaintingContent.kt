package com.devalr.startpainting.components

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.framework.components.button.GHButton
import com.devalr.framework.components.button.TopButtons
import com.devalr.framework.components.gh.GHText
import com.devalr.framework.components.gh.TextType
import com.devalr.framework.theme.GreyHunterTheme
import com.devalr.startpainting.R
import com.devalr.startpainting.components.carousel.StartPantingProjectsCarousel
import com.devalr.startpainting.model.StartPaintMiniatureVo
import com.devalr.startpainting.model.StartPaintProjectVo
import com.devalr.startpainting.model.helpers.hierotekCircleProjectVo
import com.devalr.startpainting.model.helpers.stormlightArchiveProjectVo


@Composable
fun LandscapeStartPaintingScreenContent(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    projectList: List<StartPaintProjectVo>,
    buttonEnabled: Boolean,
    onNavigateBack: () -> Unit,
    onStartPainting: () -> Unit,
    onSelectMiniature: (StartPaintMiniatureVo) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp, vertical = 60.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GHText(
                    text = stringResource(R.string.start_painting_title),
                    type = TextType.Title
                )
                Spacer(modifier = Modifier.height(16.dp))
                GHText(
                    text = stringResource(R.string.start_painting_message),
                    type = TextType.Description
                )
                Spacer(modifier = Modifier.height(32.dp))
                GHButton(
                    text = stringResource(R.string.btn_start_painting),
                    enabled = buttonEnabled
                ) {
                    onStartPainting()
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                StartPantingProjectsCarousel(
                    modifier = Modifier.fillMaxSize(),
                    projects = projectList,
                    onMiniatureSelected = onSelectMiniature
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
private fun LandscapeStartPaintingScreenContentLightModePreview() {
    GreyHunterTheme(darkTheme = false) {
        Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            LandscapeStartPaintingScreenContent(
                buttonEnabled = true,
                projectList = listOf(
                    hierotekCircleProjectVo,
                    stormlightArchiveProjectVo
                ),
                onNavigateBack = {
                    // Do nothing
                },
                onSelectMiniature = {
                    // Do nothing
                },
                onStartPainting = {
                    // Do nothing
                }
            )
        }
    }
}

@Preview(
    name = "Landscape",
    showBackground = true,
    widthDp = 891,
    heightDp = 411
)
@Composable
private fun LandscapeStartPaintingScreenContentDarkModePreview() {
    GreyHunterTheme(darkTheme = true) {
        Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            LandscapeStartPaintingScreenContent(
                buttonEnabled = true,
                projectList = listOf(
                    hierotekCircleProjectVo,
                    stormlightArchiveProjectVo
                ),
                onNavigateBack = {
                    // Do nothing
                },
                onSelectMiniature = {
                    // Do nothing
                },
                onStartPainting = {
                    // Do nothing
                }
            )
        }
    }
}