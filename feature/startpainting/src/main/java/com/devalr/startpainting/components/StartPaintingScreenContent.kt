package com.devalr.startpainting.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    onNavigateBack: () -> Unit,
    onStartPainting: () -> Unit,
    onSelectMiniature: (StartPaintMiniatureVo) -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape =
        configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        LandscapeStartPaintingScreenContent(
            innerPadding = innerPadding,
            projectList = projectList,
            buttonEnabled = buttonEnabled,
            onNavigateBack = onNavigateBack,
            onStartPainting = onStartPainting,
            onSelectMiniature = onSelectMiniature
        )
    } else {
        PortraitStartPaintingScreenContent(
            innerPadding = innerPadding,
            projectList = projectList,
            buttonEnabled = buttonEnabled,
            onNavigateBack = onNavigateBack,
            onStartPainting = onStartPainting,
            onSelectMiniature = onSelectMiniature
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StartPaintingContentScreenLightModePreview() {
    GreyHunterTheme(darkTheme = false) {
        Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {

            StartPaintingScreenContent(
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

@Preview(showBackground = true)
@Composable
private fun StartPaintingContentScreenDarkModePreview() {
    GreyHunterTheme(darkTheme = true) {
        Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            StartPaintingScreenContent(
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