package com.devalr.startpainting

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devalr.framework.components.GHButton
import com.devalr.framework.components.GHText
import com.devalr.framework.components.LoadingIndicator
import com.devalr.framework.components.TextType
import com.devalr.startpainting.components.StartPantingProjectsCarousel
import com.devalr.startpainting.interactions.Action
import com.devalr.startpainting.interactions.Action.OnSelectMiniature
import com.devalr.startpainting.interactions.Action.OnStartPainting
import com.devalr.startpainting.interactions.Event.NavigateBack
import com.devalr.startpainting.interactions.Event.NavigatePaintMiniatures
import com.devalr.startpainting.model.StartPaintMiniatureVo
import com.devalr.startpainting.model.StartPaintProjectVo
import org.koin.compose.koinInject

@Composable
fun StartPaintingScreen(
    viewModel: StartPaintingViewModel = koinInject(),
    onBackPressed: () -> Unit

) {
    val state = viewModel.uiState.collectAsState().value
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                NavigateBack -> onBackPressed()
                is NavigatePaintMiniatures -> {
                }
            }

        }
    }
    LaunchedEffect(true) { viewModel.onAction(Action.OnAppear) }

    Scaffold(
        topBar = { }
    ) { innerPadding ->
        if (state.projectsLoaded) {
            if (state.projectList.isNotEmpty()) {
                StartPaintingContentScreen(
                    projectList = state.projectList,
                    buttonEnabled = state.paintButtonEnabled,
                    onBackPressed = onBackPressed,
                    onStartPainting = {
                        viewModel.onAction(OnStartPainting)
                    },
                    onMiniatureSelected = { miniature ->
                        viewModel.onAction(OnSelectMiniature(miniature))
                    }
                )

            } else {
                //TODO: Display error
            }

        } else {
            LoadingIndicator()
        }
    }
}

@Composable
fun StartPaintingContentScreen(
    projectList: List<StartPaintProjectVo>,
    buttonEnabled:Boolean,
    onBackPressed: () -> Unit,
    onStartPainting: () -> Unit,
    onMiniatureSelected: (StartPaintMiniatureVo) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
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
        GHButton(text = "Start", enabled = buttonEnabled ) {
            onStartPainting()
        }
        StartPantingProjectsCarousel(
            projects = projectList,
            onMiniatureSelected = onMiniatureSelected
        )
    }

}
/*
@Preview(showBackground = true)
@Composable
private fun StartPaintingContentScreenPreview() {
    GreyHunterTheme(darkTheme = false) {
        StartPaintingContentScreen(
            projectList = listOf(
                hierotekCircleProject,
                stormlightArchiveProject
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
*/