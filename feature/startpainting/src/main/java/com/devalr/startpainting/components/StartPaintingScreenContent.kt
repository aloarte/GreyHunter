package com.devalr.startpainting.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.framework.components.button.GHButton
import com.devalr.framework.components.gh.GHText
import com.devalr.framework.components.ScreenSize
import com.devalr.framework.components.gh.TextType
import com.devalr.framework.components.button.TopButtons
import com.devalr.framework.components.getScreenSize
import com.devalr.framework.theme.GreyHunterTheme
import com.devalr.startpainting.R
import com.devalr.startpainting.components.carousel.StartPantingProjectsCarousel
import com.devalr.startpainting.model.StartPaintMiniatureVo
import com.devalr.startpainting.model.StartPaintProjectVo
import com.devalr.startpainting.model.helpers.hierotekCircleProjectVo
import com.devalr.startpainting.model.helpers.stormlightArchiveProjectVo

@Composable
fun StartPaintingScreenContent(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    projectList: List<StartPaintProjectVo>,
    buttonEnabled: Boolean,
    onBackPressed: () -> Unit,
    onStartPainting: () -> Unit,
    onMiniatureSelected: (StartPaintMiniatureVo) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp, vertical = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GHText(
                text = stringResource(R.string.start_painting_title),
                type = TextType.Title
            )
            Spacer(modifier = Modifier.height(10.dp))
            if (getScreenSize() != ScreenSize.SMALL) {
                GHText(
                    text = stringResource(R.string.start_painting_message),
                    type = TextType.Description
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
        StartPantingProjectsCarousel(
            modifier = Modifier.align(Alignment.Center),
            projects = projectList,
            onMiniatureSelected = onMiniatureSelected
        )
        GHButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp),
            text = stringResource(R.string.btn_start_painting),
            enabled = buttonEnabled
        ) {
            onStartPainting()
        }
        TopButtons(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(16.dp),
            onBackPressed = onBackPressed
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StartPaintingContentScreenLightModePreview() {
    GreyHunterTheme(darkTheme = false) {
        StartPaintingScreenContent(
            buttonEnabled = true,
            projectList = listOf(
                hierotekCircleProjectVo,
                stormlightArchiveProjectVo
            ),
            onBackPressed = {
                // Do nothing
            },
            onMiniatureSelected = {
                // Do nothing
            },
            onStartPainting = {
                // Do nothing
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StartPaintingContentScreenDarkModePreview() {
    GreyHunterTheme(darkTheme = true) {
        StartPaintingScreenContent(
            buttonEnabled = true,
            projectList = listOf(
                hierotekCircleProjectVo,
                stormlightArchiveProjectVo
            ),
            onBackPressed = {
                // Do nothing
            },
            onMiniatureSelected = {
                // Do nothing
            },
            onStartPainting = {
                // Do nothing
            }
        )
    }
}