package com.devalr.settings

import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.devalr.framework.components.empty.EmptyScreen
import com.devalr.framework.components.snackbar.SnackBarType
import com.devalr.framework.components.snackbar.SnackBarVisualsCustom
import com.devalr.settings.composables.SettingsScreenContent
import com.devalr.settings.interactions.Action.ChangeAppearance
import com.devalr.settings.interactions.Action.ChangeProgressColors
import com.devalr.settings.interactions.Action.ExportProjects
import com.devalr.settings.interactions.Action.ImportProjects
import com.devalr.settings.interactions.Action.Load
import com.devalr.settings.interactions.Action.Return
import com.devalr.settings.interactions.ErrorType.DatastoreRetrieval
import com.devalr.settings.interactions.ErrorType.Export
import com.devalr.settings.interactions.ErrorType.Import
import com.devalr.settings.interactions.Event.LaunchSnackBar
import com.devalr.settings.interactions.Event.NavigateBack
import com.devalr.settings.interactions.OperationType
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinInject(),
    snackBarHostState: SnackbarHostState,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val state = viewModel.uiState.collectAsState().value
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                NavigateBack -> onNavigateBack()
                is LaunchSnackBar -> {
                    launch {
                        snackBarHostState.showSnackbar(
                            SnackBarVisualsCustom(
                                message = getSnackBarMessage(context, event),
                                type = event.type
                            )
                        )
                    }
                }
            }
        }
    }

    val importLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            viewModel.onAction(ImportProjects(it))
        }
    }
    val exportProjectsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("text/csv")
    ) { uri ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
            viewModel.onAction(ExportProjects(it))
        }
    }
    LaunchedEffect(true) { viewModel.onAction(Load) }
    Scaffold { innerPadding ->
        if (state.error) {
            EmptyScreen { viewModel.onAction(Return) }
        } else {
            SettingsScreenContent(
                innerPadding = innerPadding,
                currentThemeType = state.themeType,
                progressColorType = state.progressColorConfigType,
                appVersion = state.appVersion,
                onNavigateBack = { viewModel.onAction(Return) },
                onChangeTheme = { viewModel.onAction(ChangeAppearance(it)) },
                onChangeProgressColor = { viewModel.onAction(ChangeProgressColors(it)) },
                onImportProjects = {
                    importLauncher.launch(arrayOf("*/*"))
                },
                onExportProjects = {
                    exportProjectsLauncher.launch("backup_projects.csv")
                }
            )
        }
    }
}

private fun getSnackBarMessage(context: Context, event: LaunchSnackBar): String =
    if (event.errorType != null && event.type == SnackBarType.ERROR) {
        when (event.errorType) {
            DatastoreRetrieval -> context.getString(R.string.snack_bar_datastore_retrieval_error)
            Import -> context.getString(R.string.snack_bar_imported_error)
            Export -> context.getString(R.string.snack_bar_imported_error)
        }
    } else {
        when (event.operation) {
            OperationType.Import -> context.getString(R.string.snack_bar_imported_success)
            OperationType.Export -> context.getString(R.string.snack_bar_exported_success)
        }

    }
