package com.devalr.painting

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
import com.devalr.painting.components.PaintingScreenContent
import com.devalr.painting.interactions.Action.FinishPainting
import com.devalr.painting.interactions.Action.Load
import com.devalr.painting.interactions.Action.Return
import com.devalr.painting.interactions.ErrorType
import com.devalr.painting.interactions.Event.LaunchSnackBarError
import com.devalr.painting.interactions.Event.NavigateBack
import com.devalr.painting.interactions.Event.NavigateToUpdateMiniatures
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun PaintingScreen(
    viewModel: PaintingViewModel = koinInject(),
    minisIds: List<Long>,
    onNavigateBack: () -> Unit,
    onNavigateToUpdateMiniatures: (List<Long>) -> Unit
) {
    val state = viewModel.uiState.collectAsState().value
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                NavigateBack -> onNavigateBack()
                is NavigateToUpdateMiniatures -> onNavigateToUpdateMiniatures(event.miniatureIds)
                is LaunchSnackBarError -> launch {
                    snackBarHostState.currentSnackbarData?.dismiss()
                    snackBarHostState.showSnackbar(
                        SnackBarVisualsCustom(
                            duration = SnackbarDuration.Short,
                            message = getSnackBarMessage(context, event.error),
                            type = SnackBarType.ERROR
                        )
                    )
                }
            }

        }
    }
    LaunchedEffect(true) { viewModel.onAction(Load(minisIds)) }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState) { data ->
                GHSnackBar(snackBarData = data)
            }
        }
    ) { innerPadding ->
        if (state.minisLoaded) {
            if (state.miniatures.isNotEmpty()) {
                PaintingScreenContent(
                    innerPadding = innerPadding,
                    miniatures = state.miniatures,
                    onNavigateBack = { viewModel.onAction(Return) },
                    onFinishPainting = { viewModel.onAction(FinishPainting(minisIds)) }
                )
            } else {
                //TODO: Display error
            }

        } else {
            LoadingIndicator()
        }
    }
}

private fun getSnackBarMessage(context: Context, error: ErrorType): String = when (error) {
    ErrorType.RetrievingDatabase -> context.getString(R.string.error_painting_load_database)
}

