package com.devalr.startpainting.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devalr.framework.components.GHButton
import com.devalr.framework.components.GHText
import com.devalr.framework.components.TextType
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        GHText(text = "Start painting miniatures", type = TextType.Title)
        Spacer(modifier = Modifier.height(10.dp))
        GHText(
            text = "Browse from your projects and pick the miniature or miniatures. Then, click the button to start the timer and start with your art.",
            type = TextType.Description
        )
        Spacer(modifier = Modifier.height(10.dp))
        GHButton(text = "Start", enabled = buttonEnabled) {
            onStartPainting()
        }
        StartPantingProjectsCarousel(
            projects = projectList,
            onMiniatureSelected = onMiniatureSelected
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