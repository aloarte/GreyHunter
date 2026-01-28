package com.devalr.startpainting

import android.content.Context
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.devalr.framework.components.anim.LoadingIndicator
import com.devalr.framework.components.snackbar.GHSnackBar
import com.devalr.framework.components.snackbar.SnackBarType
import com.devalr.framework.components.snackbar.SnackBarVisualsCustom
import com.devalr.startpainting.components.StartPaintingScreenContent
import com.devalr.startpainting.interactions.Action
import com.devalr.startpainting.interactions.Action.SelectMiniature
import com.devalr.startpainting.interactions.Action.StartPainting
import com.devalr.startpainting.interactions.ErrorType
import com.devalr.startpainting.interactions.ErrorType.NoMinisToPaint
import com.devalr.startpainting.interactions.ErrorType.RetrievingDatabase
import com.devalr.startpainting.interactions.Event.LaunchErrorSnackBar
import com.devalr.startpainting.interactions.Event.NavigateBack
import com.devalr.startpainting.interactions.Event.NavigateToPaintMiniatures
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun StartPaintingScreen(
    viewModel: StartPaintingViewModel = koinInject(),
    onNavigateToPaintMinis: (List<Long>) -> Unit,
    onNavigateBack: () -> Unit
) {
    val state = viewModel.uiState.collectAsState().value
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                NavigateBack -> onNavigateBack()
                is NavigateToPaintMiniatures -> onNavigateToPaintMinis(event.miniatures)
                is LaunchErrorSnackBar -> {
                    launch {
                        snackBarHostState.showSnackbar(
                            SnackBarVisualsCustom(
                                duration = SnackbarDuration.Short,
                                message = getSnackBarMessage(context, event.errorType),
                                type = SnackBarType.ERROR
                            )
                        )
                    }
                }
            }

        }
    }
    LaunchedEffect(true) { viewModel.onAction(Action.Load) }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState) { data ->
                GHSnackBar(snackBarData = data)
            }
        }
    ) { innerPadding ->
        if (state.projectsLoaded) {
            if (state.projectList.isNotEmpty()) {
                StartPaintingScreenContent(
                    innerPadding = innerPadding,
                    projectList = state.projectList,
                    buttonEnabled = state.paintButtonEnabled,
                    onNavigateBack = onNavigateBack,
                    onStartPainting = {
                        viewModel.onAction(StartPainting)
                    },
                    onSelectMiniature = { miniature ->
                        viewModel.onAction(SelectMiniature(miniature))
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

private fun getSnackBarMessage(context: Context, error: ErrorType): String =
    when (error) {
        RetrievingDatabase -> context.getString(R.string.error_start_painting_retrieving)
        NoMinisToPaint -> context.getString(R.string.error_start_painting_no_minis)
    }
