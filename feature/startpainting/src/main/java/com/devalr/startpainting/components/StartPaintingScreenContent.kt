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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.framework.components.GHButton
import com.devalr.framework.components.GHText
import com.devalr.framework.components.ScreenSize
import com.devalr.framework.components.TextType
import com.devalr.framework.components.detail.TopButtons
import com.devalr.framework.components.getScreenSize
import com.devalr.framework.theme.GreyHunterTheme
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

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp, vertical = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GHText(
                text = "Start painting miniatures",
                type = TextType.Title
            )
            Spacer(modifier = Modifier.height(10.dp))
            if (getScreenSize() != ScreenSize.SMALL) {
                GHText(
                    text = "Browse from your projects and pick the miniature or miniatures. Then, click the button to start the timer and start with your art.",
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
            text = "Start Painting",
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
private fun StartPaintingContentScreenPreview() {
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